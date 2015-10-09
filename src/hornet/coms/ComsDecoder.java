package hornet.coms;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
//import hornet.VirtualHornet;
import hornet.*;
import hornet.gui.*;

/**
 * Created by Nicholas on 4/08/2015.
 */
public class ComsDecoder {

    /** The object that manages all incoming messages */
    private VirtualHornet _virtualHornet;

    /** The queue of messages to consume */
    private BlockingQueue _toConsume = new ArrayBlockingQueue(100);

    /** The worker thread that consumes new messages */
    private Consumer _consumer;

    /**
     *
     * @param theVirtualHornet
     */
    public ComsDecoder(VirtualHornet theVirtualHornet)
    {
        _virtualHornet = theVirtualHornet;
        _consumer = new Consumer(_toConsume,_virtualHornet);
        _consumer.start();
    }

    /**
     * Called my coms when new messages are received
     * Added them to a Queue to be consumed by an external worker thread
     * @param message The message that was received
     */
    public void processMessage(byte[] message)
    {
        _toConsume.add(message);
    }
}

/**
 * Worker thread to process received messages
 */
class Consumer extends Thread {

    /** The queue of messages to consume */
    private final BlockingQueue<byte[]> _toConsume;

    /** The object that manages all incoming messages */
    private final VirtualHornet _virtualHornet;

    /**
     *
     * @param toConsume
     * @param theVirtualHornet
     */
    Consumer(BlockingQueue toConsume,VirtualHornet theVirtualHornet)
    {
        _toConsume = toConsume;
        _virtualHornet = theVirtualHornet;
    }

    /**
     * Pulls messages from the Queue
     */
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            try {
                processMessage( _toConsume.take());
            } catch (InterruptedException e) {
                System.out.println("Here 4");
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    //private int _E_firstCount =0;
    private int _E_errorCount =0;
    private int _E_lastCount =0;

    /**
     * Decodes received messages and sends them to the hornet manager
     * @param message The message to decode
     */
    public void processMessage(byte[] message)
    {
        ComPacket toTest;

        try{
            toTest = new ComPacket(message);
        }catch (Exception)
        {
            _virtualHornet.C_debugInfo(message);
            return;
        }



        if(!toTest.isValid())
        {
            _virtualHornet.C_debugInfo(message);
            return;
        }

        // check for known packet
        if (CONFIG.Coms.PacketCodes.SizeMap.get(message[2]) == null)
        {
            _virtualHornet.C_debugInfo(message);
            return; //@TODO replace with throw
        }

        // check for correct size
        if (CONFIG.Coms.PacketCodes.SizeMap.get(message[2]).get_size() != (message.length)-3)
        {
            return; //@TODO replace with throw
        }

        // monitor packet loss
        int packetCount = message[1]&0xff;
        //System.out.println(packetCount);
        if(_E_lastCount != packetCount +1)
        {
            _E_errorCount += ((packetCount - _E_lastCount)-1)&0xff;
        }
        if(packetCount < _E_lastCount)
        {
            // rollover reset
            System.out.println(_E_errorCount);
            _E_errorCount = packetCount;
        }
        _E_lastCount = packetCount;

        // get the packet
        if(message.length == 4)
        {
            _virtualHornet.C_message(message[1]);
        }
        else
        {
            byte key = message[2];
            byte[] filteredByteArray;   //created here instead of inside a case statement
            short[] sConverted;
            filteredByteArray = removeCode(message);
            sConverted = toShortArray(filteredByteArray);

            if(toTest.isValid())
            {

            }

            _virtualHornet.C_data(key,sConverted);
        }
    }

    private byte[] removeCode(byte[] message)
    {
        return Arrays.copyOfRange(message, 3, message.length-1);
    }

    private short[] toShortArray(byte[] message)
    {
        int floats = message.length/2;
        byte[] buffer = new byte[2];
        for(int i=0;i<floats;i++)
        {
            buffer[0] = message[0+(2*i)];
            buffer[1] = message[1+(2*i)];

            message[0+(2*i)] = buffer[1];
            message[1+(2*i)] = buffer[0];
        }

        final ShortBuffer fb = ByteBuffer.wrap(message).asShortBuffer();
        final short[] dst = new short[fb.capacity()];
        fb.get(dst); // Copy the contents of the FloatBuffer into dst
        return dst;
    }

    private int getCheckSum(byte[] message)
    {
        int toAdd;
        int check = 0;
        for(int i=0;i<message.length-1;i++)
        {
            toAdd = ((int)(message[i]&0xFF));
            check += ((toAdd * (i+1)) & 0xFF);
        }

        return check & 0xff;
    }
}








      /*  float[] converted;          //so that they can be used by all case statements

        switch (message[0])
        {
            case CONFIG.Coms.PacketCodes.CONNECTION_REQUEST:
                _virtualHornet.C_connectRequest();
                break;

            case CONFIG.Coms.PacketCodes.GYRO:
                if(message.length != 7)
                {
                    break;  //@TODO add error handling here
                }

                //short[] gyro = Arrays.copyOfRange(sConverted, 0, 3);
                System.out.print(sConverted[0] + " ");
                System.out.print(sConverted[1] + " ");
                System.out.println(sConverted[2]);
                _virtualHornet.C_gyro(sConverted);
                break;*/
            /*case CONFIG.Coms.PacketCodes.ACCGYRO:
               // System.out.println(message.length);
                if(message.length != 13)
                {
                    break;  //@TODO add error handling here
                }

                filteredByteArray = removeCode(message);
                sConverted = toShortArray(filteredByteArray);
                short[] acc = Arrays.copyOfRange(sConverted, 0, 3);
               // System.out.println(acc[0]);
                short[] gyro = Arrays.copyOfRange(sConverted, 3, 6);
                _virtualHornet.C_accGyro(acc, gyro);
                break;

            case CONFIG.Coms.PacketCodes.PITCH_ROLL:
                if (message.length != 5)           //Check that the size of the packet is correct
                {
                    break;  //@TODO add error handling here
                }
                filteredByteArray = removeCode(message);        //Get rid of the identification byte
                sConverted = toShortArray(filteredByteArray);    //Change from bytes to floats so packet can be used
                float accPitch = (float)sConverted[0]/10000.0f;
                float gyroRoll = (float)sConverted[1]/10000.0f;
                _virtualHornet.C_pitchRoll(accPitch, gyroRoll);

            case CONFIG.Coms.PacketCodes.LIDAR_POINT:     //When a LIDAR packet is received
                if (message.length != 9)           //Check that the size of the packet is correct
                //Currently assumed to be 1 ID byte and 4 bytes for each of the 2 floats
                {
                    break;  //@TODO add error handling here
                }
                filteredByteArray = removeCode(message);        //Get rid of the identification byte
                converted = toFloatArray(filteredByteArray);    //Change from bytes to floats so packet can be used
                float angle = converted[0];                       //Get the angle from sent packet
                float distance = converted[1];                  //Get the distance from sent packet
                _virtualHornet.L_newLidarPoint(angle, distance);//Pass angle and distance to the virtual Hornet to be
                //used by it's Lidar panel
                break;

            case CONFIG.Coms.PacketCodes.LIDAR_EOS1:     //When a LIDAR packet is received
                if (message.length != 9)           //Check that the size of the packet is correct
                //Currently assumed to be 1 ID byte and 4 bytes for each of the 2 floats
                {
                    break;  //@TODO add error handling here
                }
                filteredByteArray = removeCode(message);        //Get rid of the identification byte
                converted = toFloatArray(filteredByteArray);    //Change from bytes to floats so packet can be used
                float pitch = converted[0];                       //Get the pitch from sent packet
                float roll = converted[1];                  //Get the roll from sent packet
                _virtualHornet.L_newLidarEOS1(pitch, roll);//Pass pitch and roll to the virtual Hornet to be
                //used by it's Lidar panel
                break;

            case CONFIG.Coms.PacketCodes.LIDAR_EOS2:     //When a LIDAR packet is received
                if (message.length != 9)           //Check that the size of the packet is correct
                //Currently assumed to be 1 ID byte and 4 bytes for each of the 2 floats
                {
                    break;  //@TODO add error handling here
                }
                filteredByteArray = removeCode(message);        //Get rid of the identification byte
                converted = toFloatArray(filteredByteArray);    //Change from bytes to floats so packet can be used
                float yaw = converted[0];                       //Get the pitch from sent packet

                _virtualHornet.L_newLidarEOS2(yaw);//Pass yaw to the virtual Hornet to be
                //used by it's Lidar panel
                break;*/

//  default:
//      break;
// }