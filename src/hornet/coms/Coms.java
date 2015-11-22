package hornet.coms;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import hornet.CONFIG;
import hornet.VirtualHornet;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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

    private InputStream _in;

    /** The output stream to the port */
    private OutputStream _output;

    /** The serial port in use */
    private SerialPort _serialPort = null;

    /** The object responsible for decoding incoming communications */
    private ComsDecoder _comsDecoder;

    /**
     * Default constructor
     * @param theVirtualHornet The object responsible for interpreting messages
     */
    public Coms(VirtualHornet theVirtualHornet)
    {
        _comsDecoder = new ComsDecoder(theVirtualHornet);
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
     * Connects to the communication device (does not directly establish a link with the drone)
     * @param port The com port to connect via
     * @param baudRate The baud rate of the communications
     * @return Was the connection successful? (close is automatically called if not)
     */
    public boolean open(String port, int baudRate)
    {
        close();    //for good measure

        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find the serial port @TODO make this better
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
            _serialPort = (SerialPort) portId.open(this.getClass().getName(), 1000);

            // set port parameters
            _serialPort.setSerialPortParams( baudRate,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // open the streams
            _in = _serialPort.getInputStream();

           // _input = new BufferedReader(new InputStreamReader(_serialPort.getInputStream()));
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
        if(_serialPort == null || _output == null || _in == null)
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

    /**
     * Send a stream of bytes
     * @param toSend
     * @throws IOException
     */
    public void send(byte[] toSend) throws IOException {
        if(_output != null)
        {
            _output.write(toSend);
        }

    }

    //ArrayList<Byte> _read = new ArrayList<>();


    byte[] _readBuffer = new byte[100];
    int _readCount;

    /**
     * Handle an event on the serial port. Read the data and decode it.
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                while(_in.available() !=0)
                {
                    int read = _in.read();

                    if(read == '\n')
                    {
                        byte[] toSend = Arrays.copyOfRange(_readBuffer, 0, _readCount);
                        _comsDecoder.processMessage(toSend);
                        _readCount =0;
                    }
                    else
                    {
                        _readBuffer[_readCount] = (byte)(read & 0xFF);
                        _readCount++;
                    }
                }

               /* String inputLine=_input.readLine();
                _comsDecoder.processMessage(inputLine.getBytes());*/
                //char[] dump = new char[100];
                //System.out.println(_input.read(dump));

            } catch (Exception e) {
                // this does happen occasionally but it's not known why
            }
        }

        // Ignore all the other eventTypes, but you should consider the other ones.
    }

}














    /*public void send(DataPacket toSend) throws IOException {
        ComPacket thePacket = new ComPacket(toSend,_sendCount);
        _sendCount++;
        _sendCount = _sendCount &0xff;
        _output.write(thePacket.getPacket());
    }
*/