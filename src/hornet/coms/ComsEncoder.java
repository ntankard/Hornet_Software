package hornet.coms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import hornet.CONFIG;
import hornet.gui.Navigation;

import javax.xml.crypto.Data;

/**
 * Created by Nicholas on 4/08/2015.
 */
public class ComsEncoder {

    /** The object used to send messages */
    private Coms _coms;

    /** The thread used to send data */
    private Sender _sender;

    /** An array of messages to send */
    private ArrayBlockingQueue<DataPacket> _toUpdate;

    /**
     *
     * @param theComs
     */
    public ComsEncoder(Coms theComs)
    {
        _toUpdate = new ArrayBlockingQueue<>(1000);

        _coms = theComs;
        _sender = new Sender(_toUpdate,_coms);
        _sender.start();
    }

    /**
     * Add new data to send
     * @param toSend
     */
    public void send_data(DataPacket toSend)
    {
        _toUpdate.add(toSend);
        System.out.println("Add");
    }
}

/**
 * Worker thread to send messages
 */
class Sender extends Thread {

    /** A shared, thread safe queue of new data to send */
    private ArrayBlockingQueue<DataPacket> _toAdd;

    /** The current set of data to send */
    private ArrayList<DataPacket> _buffer;

    /** The com object to send through */
    private Coms _coms;

    /** THe count of the last sent packet */
    private int _sendCount =0;

    /**
     * @param toSend  The Queue of messages to send
     * @param theComs The com object to send them through
     */
    Sender(ArrayBlockingQueue<DataPacket> toSend, Coms theComs) {
        _toAdd = toSend;
        _coms = theComs;
        _buffer = new ArrayList<>();
    }

    int sendI =0;

    /**
     * Pulls messages from the queue
     */
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {

            // transfer pending messages into the buffer
            while (!_toAdd.isEmpty()) {
                DataPacket toAdd = _toAdd.remove();
                boolean found = false;

                for (int i = 0; i < _buffer.size(); i++) {
                    if (_buffer.get(i).getID() == toAdd.getID()) {
                        _buffer.set(i,toAdd);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    _buffer.add(toAdd);
                }
            }

            sendI++;
            if(sendI >= _buffer.size())
            {
                sendI=0;
            }

            // send the packets
            //for (int i = 0; i < _buffer.size(); i++) {
            if(_buffer.size() !=0) {
                try {
                    send(_buffer.get(sendI));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                /*try {
                    if(_buffer.size() !=0) {
                        send(_buffer.get(sendI));
                    }
                   // wait(1);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                   // return;
                }*/
            //}
        }
    }

    /**
     * Converts a DataPacket to a byte stream and sends it via the Com device
     * @param toSend
     * @throws IOException
     */
    private void send(DataPacket toSend) throws IOException {
        if(_coms.isConnected()) {
            ComPacket packet = new ComPacket(toSend, _sendCount);

            _coms.send(packet.getPacket());

            _sendCount++;
            _sendCount = _sendCount & 0xFF;
        }
    }
}

