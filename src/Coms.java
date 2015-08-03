import com.digi.xbee.api.exceptions.XBeeException;
import gnu.io.CommPortIdentifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

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

    public void Coms(ComsDecoder theComsDecoder)
    {
        _comsDecoder = theComsDecoder;
    }

    public boolean open(String port, int baudRate) {return false;}

    public void close() {}

    public void send(byte[] dataToSend) throws IOException {}

    protected ComsDecoder _comsDecoder;

}
