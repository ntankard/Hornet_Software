import com.digi.xbee.api.RemoteXBeeDevice;
import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.listeners.IDataReceiveListener;
import com.digi.xbee.api.models.XBee64BitAddress;
import com.digi.xbee.api.models.XBeeMessage;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Nicholas on 3/08/2015.
 */
public class XBee_Series1 extends Coms {

    public XBee_Series1(VirtualHornet vh)
    {
        _virtualHornet = vh;
    }

    public boolean open(String port, int baudRate)
    {

        // connect to the xbee
        _xbee = new XBeeDevice(port, baudRate);
        try {
            _xbee.open();
        } catch (XBeeException e) {
            return false;
        }

        // setup the thread to send data
        _toSend = new ArrayBlockingQueue(100);
        _sender = new Sender(_toSend,_xbee);
        _sender.start();

        //setup the thread to read data
        _xbee.addDataListener(new IDataReceiveListener() {
            @Override
            public void dataReceived(XBeeMessage xBeeMessage) {
                if(xBeeMessage!= null)
                {
                    //@TODO notify somone
                    System.out.println(xBeeMessage.getDataString());
                }
            }
        });

        return true;
    }

    public void close()
    {
        _sender.interrupt();
        while(_sender.isAlive()){}

        _xbee.close();
    }

    public void send(byte[] dataToSend)
    {
        _toSend.add(dataToSend);
    }

    private XBeeDevice _xbee;
    private Sender _sender;
    private BlockingQueue _toSend = new ArrayBlockingQueue(100);
}

class Sender extends Thread {

    private final BlockingQueue _toSend;
    private final XBeeDevice _xbee;
    private final RemoteXBeeDevice _drone;

    Sender(BlockingQueue toSend,XBeeDevice xbee)
    {
        _toSend = toSend;
        _xbee = xbee;
        _drone = new RemoteXBeeDevice(_xbee,new XBee64BitAddress(0,0,0,0,0,0,0,0));
    }

    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            try {
                send(_toSend.take());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    void send(Object x)
    {
        for(int i=0;i<3;i++) {  //@TODO magic humber
            try {
                _xbee.sendData(_drone, (byte[]) x);
                return; // sent corectly
            } catch (XBeeException e) {
                continue;
            }
        }

        // sending failed
        //@TODO notify sompthing
    }
}
