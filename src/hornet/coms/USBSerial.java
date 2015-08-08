package hornet.coms;


import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import hornet.CONFIG;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

/**
 * Created by Nicholas on 4/08/2015.
 */
public class USBSerial extends Coms implements SerialPortEventListener {

    /**
     * A BufferedReader which will be fed by a InputStreamReader
     * converting the bytes into characters
     * making the displayed results codepage independent
     */
    private BufferedReader _input;

    /** The output stream to the port */
    private OutputStream _output;

    /** The serial port in use */
    private SerialPort _serialPort;

    /**
     *
     * @param theComsDecoder The object responsible for interpretingng messages
     */
    public USBSerial(ComsDecoder theComsDecoder)
    {
        _comsDecoder = theComsDecoder;
    }

    /**
     * Attempts to open a specified serial port and setup all required resources
     * @param port
     * @param baudRate
     */
    public boolean open(String port, int baudRate)
    {
        close();    //for good measure

        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find the serial port @TODO make this less stupid
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            if (currPortId.getName().equals(port)) {
                portId = currPortId;
                break;
            }
        }
        if (portId == null) {
            return false;
        }

        try {
            // open serial port, and use class name for the appName.
            _serialPort = (SerialPort) portId.open(this.getClass().getName(), CONFIG.Coms.USBSerial.TIME_OUT);

            // set port parameters
            _serialPort.setSerialPortParams( baudRate,
                                            SerialPort.DATABITS_8,
                                            SerialPort.STOPBITS_1,
                                            SerialPort.PARITY_NONE);

            // open the streams
            _input = new BufferedReader(new InputStreamReader(_serialPort.getInputStream()));
            _output = _serialPort.getOutputStream();

            // add event listeners
            _serialPort.addEventListener(this);
            _serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            close();
            return false;
        }

        return true;
    }

    /**
     * Safely sever the connection to the serial port
     */
    public synchronized void close()
    {
        if (_serialPort != null) {
            _serialPort.removeEventListener();
            _serialPort.close();
        }
    }

    @Override
    public boolean canSend() {
        return true;
    }

    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine=_input.readLine();
                _comsDecoder.processMessage(inputLine.getBytes());
            } catch (Exception e) {
                System.err.println(e.toString());   //@TODO this is a problem
            }
        }
        // Ignore all the other eventTypes, but you should consider the other ones.
    }

    /**
     * Sends a single packet
     * @param dataToSend The packet to send
     * @throws IOException Write failed
     */
    public void send(byte[] dataToSend) throws IOException {
        byte[] toSend = new byte[dataToSend.length+1];
        for(int i=0;i<dataToSend.length;i++)
        {
            toSend[i] = dataToSend[i];
        }
        toSend[dataToSend.length] = '\n';
        _output.write(toSend);
    }

}
