import com.digi.xbee.api.RemoteXBeeDevice;
import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.listeners.IDataReceiveListener;
import com.digi.xbee.api.models.XBee64BitAddress;
import com.digi.xbee.api.models.XBeeMessage;
import gnu.io.CommPortIdentifier;

import java.util.ArrayList;

import java.util.Enumeration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Nicholas on 28/07/2015.
 */
    public class Coms {

    static public ArrayList<String> getPorts()
    {
        ArrayList<String> toReturn = new ArrayList<String>();

        Enumeration enu_ports  = CommPortIdentifier.getPortIdentifiers();

        while (enu_ports.hasMoreElements()) {
            CommPortIdentifier port_identifier = (CommPortIdentifier) enu_ports.nextElement();

            if(port_identifier.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {
                toReturn.add(port_identifier.getName());
            }
        }

        return toReturn;
    }


    public void open(String port, int baudRate) throws XBeeException {

        // connect to the xbee
        _xbee = new XBeeDevice(port, baudRate);
        _xbee.open();

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

