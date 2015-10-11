package hornet;

import hornet.coms.Coms;
import hornet.coms.ComsEncoder;
import hornet.coms.DataPacket;
import hornet.gui.Navigation;
import hornet.gui.rootPanels.RP_ComSettings;
import hornet.joystrick.JoystickManager;


/**
 * Created by Nicholas on 2/08/2015.
 */
public class VirtualHornet {

    /** The system that manages low level communications */
    private Coms _coms;

    /** The system that encodes messages into spendable packets */
    private ComsEncoder _comsEncoder;

    /** The UI */
    private Navigation _navigation;

    /** THe Joystick Manager */
    private JoystickManager _joystickManager;

    /** The state of the software system */
    //enum State{Init,Idle,Connect,Connected}
    //private State _state;

    private boolean _joyReady = false;

    // -----------------------------------------------------------------------------------------------------------------
    // ################################################# SETUP #########################################################
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Default constructor
     * Dose nothing because components are not added yet
     */
    public VirtualHornet()
    {
       // _state =State.Init;
    }

    /**
     * Attach all components that need to be constructed externally
     */
    public void attachNavigation(Navigation theNav){ _navigation = theNav; }
    public void attachComs(Coms theComs){_coms = theComs;}
    public void attachComsEncoder(ComsEncoder theComsEncoder){_comsEncoder = theComsEncoder;}
    public void attachJoystickManager(JoystickManager theJoystickManager){_joystickManager = theJoystickManager;}

    /**
     * Start the system (equivalent to a constructor, call after all components are attached)
     */
    public void start()
    {
        _navigation.open();
        _navigation.setComPorts(Coms.getPorts());

        //_state =State.Idle;
        _navigation.start();
       // _navigation.updateJoystickList();

   }

    // -----------------------------------------------------------------------------------------------------------------
    // ################################################# COMS ##########################################################
    // -----------------------------------------------------------------------------------------------------------------

    public void newData(DataPacket data)
    {

    }

    public void C_debugInfo(byte[] message)
    {
        _navigation.newDebugData(message);
    }

    public void C_data(byte key,short[] data)
    {
        _navigation.newData(key,data);

        if(CONFIG.Coms.PacketCodes.SizeMap.get(key).is_toHornet())
        {
            _comsEncoder.send_data(key,data);
        }

        switch(key)
        {
            case CONFIG.Coms.PacketCodes.STATUS:
                _navigation.setConnectionState(RP_ComSettings.ConnectionState.Connected);
                break;
        }

       /* if(key == 121)
        {
            _navigation.gyro(data);
        }*/
    }

    public void C_message(byte key)
    {

    }

    // -----------------------------------------------------------------------------------------------------------------
    // ############################################## UI EVENTS ########################################################
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The user is attempting to connect to the drone
     * @param comPort The com port to use to connect to the drone
     */
    public void UI_connect(String comPort,int baudRate ){

        if(!_coms.isConnected())
        {
            _navigation.setConnectionState(RP_ComSettings.ConnectionState.Connecting);
            if(!_coms.open(comPort,baudRate))
            {
                _navigation.setConnectionState(RP_ComSettings.ConnectionState.Disconnected);
            }
        }


       /* if(_state == State.Idle) {
            _navigation.setConnectionState(RP_ComSettings.ConnectionState.Connecting);
            _state =  State.Connect;
            if(!_coms.open(comPort,baudRate))
            {
                _navigation.setConnectionState(RP_ComSettings.ConnectionState.Disconnected);
                //@TODO add failure notification
            }
         }else
         {
             //@toDO throw
         }*/
    }

    public void UI_disconnect()
    {
        _coms.close();
        _navigation.setConnectionState(RP_ComSettings.ConnectionState.Disconnected);
    }

    public void UI_joystickConnected(String theJoystick)
    {
        if(_joystickManager.connect(theJoystick))
        {
           // _navigation.turnJoyStickConnectedOn();
            _joyReady = false;
        }
    }

    public void UI_refreshSerialPort()
    {

    }

    // -----------------------------------------------------------------------------------------------------------------
    // ########################################### JOYSTICKS EVENTS ####################################################
    // -----------------------------------------------------------------------------------------------------------------

    /*public void J_newXY(int xPer, int yPer)
    {
        if(_state != State.Init) {
          //  _navigation.updateJoystickXY(xPer, yPer);
        }
    }

    public void J_newRotation(int r)
    {

        if(_state != State.Init)
        {
          //  _navigation.updateJoystickRotation(r);
        }
    }

    public void J_newThrottle(int t)
    {
        if(_state != State.Init)
        {
           // _navigation.updateJoystickThrottle(t);
            if(t == 0)
            {
                _joyReady = true;
                _navigation.turnJoyStickReady();
            }

            if(_state == State.Connected && _joyReady)
            {
                //_comsEncoder.send_throttle(t);
            }
        }
    }*/

    public void J_armDisarm()
    {
        if(_joystickManager.isConnected())
        {
            if(_joystickManager.isSafe()) {
                _comsEncoder.send_command(CONFIG.Coms.PacketCodes.ARM_DISARM);
            }
        }

       /* if(_state == State.Connected && _joyReady)
        {
            _comsEncoder.send_armDisarm();
        }*/
    }

}




/**
 * A message has been received from the drone requesting to open a connection
 */
    /*public void C_connectRequest()
    {
        if(_state == State.Connect)
        {
            _state = State.Connected;
            _navigation.setConnectionState(Navigation.ConnectionState.Connected);
            _comsEncoder.send_connectionConfirmation();
            _comsEncoder.send_connectionConfirmation();
        }
    }

    public void C_accGyro(short[] acc,short[] gyro)
    {
        _navigation.accGyro(acc,gyro);
        if(_state == State.Connect)
        {
            _comsEncoder.send_reset();
        }
    }

    public void C_pitchRoll(float pitch,float roll)
    {
        _navigation.pitchRoll(pitch, roll);
        if(_state == State.Connect)
        {
            _comsEncoder.send_reset();
        }
    }

    public void C_gyro(short[] gyro)
    {
        _navigation.gyro(gyro);
        if(_state == State.Connect)
        {
          //  _comsEncoder.send_reset();
        }
    }*/








    /*public void L_newLidarPoint(float angle, float distance)
    {
        //_navigation.newLidar(yaw, distance, pitch);     //Pass the newly received Lidar data to the UI to be drawn
        _lidar.addPoint(angle,distance);
    }

    public void L_newLidarEOS1(float pitch, float roll)
    {
        if(_EOS1 == false) {
            _pitch = pitch;
            _roll = roll;
            _EOS1 = true;
        }
        else {
        //@TODO throw error
        }
    }

    public void L_newLidarEOS2(float yaw)
    {
        if(_EOS1 == true) {

            _EOS1 = false;
            _lidar.addEOSweep(_pitch,_roll, yaw);

        }
            else {
                //@TODO throw error
            }
        }

    public void getSweepPoints(ArrayList<XYPoint> sweepPoints)
    {
       // _navigation.updateLidarTopView(sweepPoints);
    }*/