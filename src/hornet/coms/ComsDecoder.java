package hornet.coms;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
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

    private Navigation _navigation;

    /**
     *
     * @param theVirtualHornet
     */
    public ComsDecoder(VirtualHornet theVirtualHornet,Navigation theNavigation)
    {
        _virtualHornet = theVirtualHornet;
        _navigation = theNavigation;
        _consumer = new Consumer(_toConsume,_virtualHornet);
        _consumer.start();
    }

    /**
     * Called my coms when new messages are received
     * Added them to a Queue to be consumed by an external worker thread
     * @param message The message that was recieved
     */
    public void processMessage(byte[] message)
    {
        _toConsume.add(message);
        try {
            _navigation.newReceivedMessage(new String(message, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    /**
     * Decodes received messages and sends them to the hornet manager
     * @param message The message to decode
     */
    public void processMessage(byte[] message)
    {
        if(message.length ==0)
        {
            return; //@TODO replace with throw
        }

        byte[] filteredByteArray;   //created here instead of inside a case statement
        float[] converted;          //so that they can be used by all case statements

        switch (message[0])
        {
            case CONFIG.Coms.PacketCodes.CONNECTION_REQUEST:
                _virtualHornet.C_connectRequest();
                break;
            case CONFIG.Coms.PacketCodes.ACCGYRO:
                if(message.length != 25)
                {
                    break;  //@TODO add error handling here
                }
                filteredByteArray = removeCode(message);
                converted = toFloatArray(filteredByteArray);
                float[] acc = Arrays.copyOfRange(converted, 0, 3);
                float[] gyro = Arrays.copyOfRange(converted, 3, 6);
                _virtualHornet.C_accGyro(acc, gyro);
                break;

            case CONFIG.Coms.PacketCodes.PITCH_ROLL:
                if (message.length != 9)           //Check that the size of the packet is correct
                {
                    break;  //@TODO add error handling here
                }
                filteredByteArray = removeCode(message);        //Get rid of the identification byte
                converted = toFloatArray(filteredByteArray);    //Change from bytes to floats so packet can be used
                float accPitch = converted[0];
                float gyroRoll = converted[1];
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
                break;

            default:
                break;
        }
    }

    private byte[] removeCode(byte[] message)
    {
        return Arrays.copyOfRange(message, 1, message.length);
    }

    private float[] toFloatArray(byte[] message)
    {
        final FloatBuffer fb = ByteBuffer.wrap(message).asFloatBuffer();
        final float[] dst = new float[fb.capacity()];
        fb.get(dst); // Copy the contents of the FloatBuffer into dst
        return dst;
    }
}