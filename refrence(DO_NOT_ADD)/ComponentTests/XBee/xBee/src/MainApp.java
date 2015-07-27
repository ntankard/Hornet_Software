import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress16;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.wpan.TxRequest16;

import com.rapplogic.xbee;

public class MainApp {
    /* Constants */
   /* // TODO Replace with the port where your sender module is connected to.
    private static final String PORT = "COM12";
    // TODO Replace with the baud rate of your sender module.
    private static final int BAUD_RATE = 9600;

    private static final String DATA_TO_SEND = "Hello XBee World!";
    private static final byte[] dataToSend = DATA_TO_SEND.getBytes();

    private static final XBee16BitAddress test1 = new XBee16BitAddress(0,0);
    private static final TxRequest16 test = new TxRequest16(test1,dataToSend);

*/

    public static void main(String[] args) throws XBeeException {

        XBee xbee = new XBee();
        xbee.open("COM5", 9600);
        int[] payload = new int[] { 90, 180 };
        XBeeAddress16 add = new XBeeAddress16(0,0);
        TxRequest16 req = new TxRequest16(add,payload);

        xbee.sendAsynchronous(req);


       /* XBeeDevice myDevice = new XBeeDevice(PORT, BAUD_RATE);


        // Note: we are using the Java int data type, since the byte data type is not unsigned, but the payload is limited to bytes.  That is, values must be between 0-255.
        int[] payload = new int[] { 90, 180 };

// specify the remote XBee 16-bit MY address
        XBee16BitAddress destination = new XBee16BitAddress(0x00, 0x00);



        //TX16Packet tx = new TX16Packet()



       // TxStatusResponse status = (TxStatusResponse) xbee.sendSynchronous(tx);



        //byte[] dataToSend = DATA_TO_SEND.getBytes();

        try {
            myDevice.open();

            System.out.format("Sending broadcast data: '%s'", new String(dataToSend));

            myDevice.sendPacket(test);

            //myDevice.sendBroadcastData(dataToSend);

            System.out.println(" >> Success");

        } catch (XBeeException e) {
            System.out.println(" >> Error");
            e.printStackTrace();
            System.exit(1);
        } finally {
            myDevice.close();
        }*/
    }
}
