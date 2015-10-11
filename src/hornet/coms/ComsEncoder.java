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

    private Coms _coms;
    private Sender _sender;
    private ArrayBlockingQueue<DataPacket> _toUpdate;

    public ComsEncoder(Coms theComs)
    {
        _toUpdate = new ArrayBlockingQueue<>(10);

        _coms = theComs;
        _sender = new Sender(_toUpdate,_coms);
        _sender.start();
    }

    public void send_data(DataPacket toSend)
    {
        _toUpdate.add(toSend);
    }
}

/**
 * Worker thread to send messages
 */
class Sender extends Thread {

    /**
     * The messages to send
     */
    private ArrayBlockingQueue<DataPacket> _toAdd;

    private ArrayList<DataPacket> _buffer;

    /**
     * The com object to send through
     */
    private Coms _coms;

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

            // send the packets
            for (int i = 0; i < _buffer.size(); i++) {
                try {
                    send(_buffer.get(i));
                    wait(1);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private void send(DataPacket toSend) throws IOException {
        ComPacket packet = new ComPacket(toSend,_sendCount);

        _coms.send(packet.getPacket());

        _sendCount++;
        _sendCount = _sendCount & 0xFF;
    }

}

