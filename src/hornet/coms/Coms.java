package hornet.coms;

import gnu.io.CommPortIdentifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created by Nicholas on 28/07/2015.
 */
abstract public class Coms {

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

            if(port_identifier.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {
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
    abstract public boolean open(String port, int baudRate);

    /**
     * Safely close a connection
     */
    abstract public void close();

    /**
     * Is the com chanel clear to send
     * @return Can you send?
     */
    abstract public boolean canSend();

    /**
     * Send a single packet (only call if canSend == false)
     * @param dataToSend The packet to send
     * @throws IOException
     */
    abstract public void send(byte[] dataToSend) throws IOException;

    /** The object responsible for decoding incoming communications */
    protected ComsDecoder _comsDecoder;
}
