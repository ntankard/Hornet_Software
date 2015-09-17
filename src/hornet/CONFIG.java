package hornet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nicholas on 28/07/2015.
 */
public class CONFIG {

    public static class Coms{

        public static class PacketCodes
        {
            public static final byte CONNECTION_REQUEST = 'a';
            public static final byte GYRO = 'y';
            public static final byte DEBUG = 'd';
            public static final byte JOY_XY = 'j';
            public static final byte JOY_THROTTLE = 't';
            public static final byte JOY_Z = 'z';
            public static final byte STATUS = 's';

            public static final int CONTROL_PRI =1;

            public static final Map<Byte, PacketInfo> SizeMap = new HashMap<>();
            static {
                SizeMap.put(GYRO, new PacketInfo(7,false,-1));
                SizeMap.put(CONNECTION_REQUEST, new PacketInfo(1,false,1));
                SizeMap.put(JOY_XY,new PacketInfo(5,true,CONTROL_PRI));
                SizeMap.put(JOY_THROTTLE,new PacketInfo(3,true,CONTROL_PRI));
                SizeMap.put(JOY_Z,new PacketInfo(3,true,CONTROL_PRI));
                SizeMap.put(STATUS,new PacketInfo(3,true,-1));
            }
        }

        public class USBSerial
        {
            /** Milliseconds to block while waiting for port open */
            public static final int TIME_OUT = 2000;
        }
    }


   // public class XBEE
   // {
   //     public final int BAUD_RATE = 9600;
   // }
}
