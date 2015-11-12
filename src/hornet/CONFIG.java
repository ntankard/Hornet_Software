package hornet;

/**
 * Created by Nicholas on 28/07/2015.
 */
public class CONFIG {

    public static class Coms{

        public static class PacketCodes
        {
            public static final byte GYRO = 1;
            public static final byte STATUS = 2;
            public static final byte LIDAR_POINT = 3;
            public static final byte COMPENSATOR_VECTOR =4;
            public static final byte JOY_VECTOR = 5;
            public static final byte TOTAL_VECTOR = 6;
            public static final byte SCHEDULAR_MONITOR = 7;

            public static final byte JOY_XY = 100;
            public static final byte JOY_THROTTLE = 101;
            public static final byte JOY_Z = 102;
            public static final byte ARM_DISARM = 103;
            public static final byte AVOID_ONOFF = 104;

            public static final byte LOCAL_PACKET_LOSS = (byte)255;

            //public static final byte NULL_PACKET = (byte)0xff;

            //public static final byte RECEIVE_ERROR_COUNT = 'r';


            //public static final byte CONNECTION_REQUEST = 'a';
           // public static final byte DEBUG = 'd';





           /* public static final int CONTROL_PRI =1;

            public static final Map<Byte, PacketInfo> SizeMap = new HashMap<>();
            static {
                SizeMap.put(GYRO, new PacketInfo(7,false,-1));
                //SizeMap.put(CONNECTION_REQUEST, new PacketInfo(1,false,1));
                SizeMap.put(JOY_XY,new PacketInfo(5,true,CONTROL_PRI));
                SizeMap.put(JOY_THROTTLE,new PacketInfo(3,true,CONTROL_PRI));
                SizeMap.put(JOY_Z,new PacketInfo(3,true,CONTROL_PRI));
                SizeMap.put(STATUS,new PacketInfo(5,false,-1));
               // SizeMap.put(MOTOR_STATUS,new PacketInfo(9,false,-1));
                SizeMap.put(ARM_DISARM,new PacketInfo(1,true,0));
                SizeMap.put(NULL_PACKET,new PacketInfo(1,false,0));
            }
            */
        }

        /*public class USBSerial
        {
            public static final int TIME_OUT = 2000;
        }*/
    }


   // public class XBEE
   // {
   //     public final int BAUD_RATE = 9600;
   // }
}
