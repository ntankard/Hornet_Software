package hornet.coms;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import hornet.CONFIG;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created by Nicholas on 28/07/2015.
 */
public class Coms implements SerialPortEventListener {

    /**
     * A BufferedReader which will be fed by a InputStreamReader
     * converting the bytes into characters
     * making the displayed results codepage independent
     */
    private BufferedReader _input;

    /** The output stream to the port */
    private OutputStream _output;

    /** The serial port in use */
    private SerialPort _serialPort = null;

    /** The object responsible for decoding incoming communications */
    private ComsDecoder _comsDecoder;

    /** The number of bytes already sent (used for packet validation in the firmware) */
   // private int _sendCount =0;

    /**
     * Default constructor
     * @param theComsDecoder The object responsible for interpreting messages
     */
    public Coms(ComsDecoder theComsDecoder)
    {
        _comsDecoder = theComsDecoder;
    }

    /**
     * Gets a list of available serial com ports
     * @return A list of available ports
     */
    static public ArrayList<String> getPorts()
    {
        ArrayList<String> toReturn = new ArrayList<>();

        Enumeration enu_ports  = CommPortIdentifier.getPortIdentifiers();

        while (enu_ports.hasMoreElements()) {
            CommPortIdentifier port_identifier = (CommPortIdentifier) enu_ports.nextElement();

            if (port_identifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                toReturn.add(port_identifier.getName());
            }
        }
        return toReturn;
    }

    /**
     * Connects to the communication device (dose not directly establish a link with the drone.
     * @param port The com port to connect via
     * @param baudRate The baud rate of the communications
     * @return Was the connection successful? (close is automatically called if not)
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
     * Safely close a connection
     */
    public synchronized void close()
    {
        if (_serialPort != null) {
            _serialPort.removeEventListener();
            _serialPort.close();
            _serialPort = null;
        }
    }

    /**
     * Is a serial connection established?
     * @return true if a serial connection established?
     */
    public boolean isConnected()
    {
        if(_serialPort == null)
        {
            return false;
        }
        try {
            _serialPort.getInputStream();
        }catch (Exception e)
        {
            return false;
        }

        return true;
    }

    public void send(byte[] toSend) throws IOException {
        _output.write(toSend);
    }

    /*public void send(DataPacket toSend) throws IOException {
        ComPacket thePacket = new ComPacket(toSend,_sendCount);
        _sendCount++;
        _sendCount = _sendCount &0xff;
        _output.write(thePacket.getPacket());
    }
*/
    /**
     * Handle an event on the serial port. Read the data and decode it.
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine=_input.readLine();
                _comsDecoder.processMessage(inputLine.getBytes());
            } catch (Exception e) {
                // this dose happen occasionally but its not known why
            }
        }
        // Ignore all the other eventTypes, but you should consider the other ones.
    }

}
