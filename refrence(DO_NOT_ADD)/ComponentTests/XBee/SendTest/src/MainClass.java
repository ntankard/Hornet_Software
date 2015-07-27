import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress16;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.wpan.TxRequest16;

/**
 * Created by Nicholas on 27/07/2015.
 */


public class MainClass {

    public static void main(String[] args) throws XBeeException {
        XBee xbee = new XBee();
        xbee.open("COM5", 9600);
        int[] payload = new int[] { 90, 180 };
        XBeeAddress16 add = new XBeeAddress16(0,0);
        TxRequest16 req = new TxRequest16(add,payload);

        xbee.sendAsynchronous(req);
    }
}
