package hornet;

import hornet.coms.Coms;
import hornet.coms.ComsEncoder;
import hornet.coms.DataPacket;
import hornet.gui.Navigation;
import hornet.gui.rootPanels.RP_ComSettings;
import hornet.joystrick.JoystickManager;
import hornet.lidar.LidarManager;
import hornet.lidar.XYPoint;


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

    private LidarManager _lidarManager;

    private boolean _isArmed;

    // -----------------------------------------------------------------------------------------------------------------
    // ################################################# SETUP #########################################################
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Default constructor
     * Dose nothing because components are not added yet
     */
    public VirtualHornet() {}

    /**
     * Attach all components that need to be constructed externally
     */
    public void attachNavigation(Navigation theNav){ _navigation = theNav; }
    public void attachComs(Coms theComs){_coms = theComs;}
    public void attachComsEncoder(ComsEncoder theComsEncoder){_comsEncoder = theComsEncoder;}
    public void attachJoystickManager(JoystickManager theJoystickManager){_joystickManager = theJoystickManager;}
    public void attachLidar(LidarManager theLidarManager) { _lidarManager = theLidarManager;}


    /**
     * Start the system (equivalent to a constructor, call after all components are attached)
     */
    public void start()
    {
        _navigation.open();
        _navigation.setComPorts(Coms.getPorts());

        _navigation.start();

        J_disarm();
        J_avoidOff();
   }

    // -----------------------------------------------------------------------------------------------------------------
    // ################################################# COMS ##########################################################
    // -----------------------------------------------------------------------------------------------------------------

    public void newDataIn(DataPacket data)
    {
        _navigation.newDataIn(data);
        switch (data.getID())
        {
            case CONFIG.Coms.PacketCodes.LIDAR_POINT:
                _lidarManager.newPoint(data);
                break;
        }
    }

    public void newDataOut(byte key,short[] data)
    {
        DataPacket toSend = new DataPacket(key,data);
        newDataOut(toSend);
    }

    public void newDataOut(DataPacket data)
    {
        // prevent joystick commands going through if the system is disarmed
        if(!_isArmed)
        {
            if(     data.getID() == CONFIG.Coms.PacketCodes.JOY_THROTTLE ||
                    data.getID() == CONFIG.Coms.PacketCodes.JOY_XY ||
                    data.getID() == CONFIG.Coms.PacketCodes.JOY_Z)
            {
                return;
            }
        }

        _navigation.newDataOut(data);
        _comsEncoder.send_data(data);
    }

    public void newCorruptPacket(byte[] message)
    {
        _navigation.newDebugData(message);
    }

    public void newPacketLoss(double loss)
    {
        _navigation.newPacketLoss(loss);
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
           // _joyReady = false;
        }
    }

    public void UI_refreshSerialPort()
    {

    }

    // -----------------------------------------------------------------------------------------------------------------
    // ########################################### JOYSTICKS EVENTS ####################################################
    // -----------------------------------------------------------------------------------------------------------------

    /*public void J_armDisarm()
    {
        if(_joystickManager.isConnected())
        {
            if(_joystickManager.isSafe()) {
                //_comsEncoder.send_command(CONFIG.Coms.PacketCodes.ARM_DISARM);
            }
        }
    }*/

    public void J_arm()
    {
        if(_joystickManager.isConnected())
        {
            if(_joystickManager.isSafe()) {

                _navigation.setArmDisarm(true);
                _isArmed = true;

                // set the arm state to disarm
                short[] toSendData = new short[1];
                toSendData[0] =1;
                DataPacket toSend = new DataPacket(CONFIG.Coms.PacketCodes.ARM_DISARM,toSendData);
                newDataOut(toSend);
            }
        }
    }

    public void J_avoidOn()
    {
        short[] toSendData = new short[1];
        toSendData[0] =1;
        DataPacket toSend = new DataPacket(CONFIG.Coms.PacketCodes.AVOID_ONOFF,toSendData);
        newDataOut(toSend);
    }

    public void J_avoidOff()
    {
        short[] toSendData = new short[1];
        toSendData[0] =0;
        DataPacket toSend = new DataPacket(CONFIG.Coms.PacketCodes.AVOID_ONOFF,toSendData);
        newDataOut(toSend);
    }

    public void J_disarm()
    {
        // allow the current joystick commands through
        _isArmed = true;

        // set XY to 50
        short[] toSendData = new short[2];
        toSendData[0] =50;
        toSendData[1] = 50;
        DataPacket toSend = new DataPacket(CONFIG.Coms.PacketCodes.JOY_XY,toSendData);
        newDataOut(toSend);

        // set z to 50
        toSendData = new short[1];
        toSendData[0] =50;
        toSend = new DataPacket(CONFIG.Coms.PacketCodes.JOY_Z,toSendData);
        newDataOut(toSend);

        // set throttle to 0
        toSendData = new short[1];
        toSendData[0] =0;
        toSend = new DataPacket(CONFIG.Coms.PacketCodes.JOY_THROTTLE,toSendData);
        newDataOut(toSend);

        // set the arm state to disarm
        toSendData = new short[1];
        toSendData[0] =0;
        toSend = new DataPacket(CONFIG.Coms.PacketCodes.ARM_DISARM,toSendData);
        newDataOut(toSend);

        _navigation.setArmDisarm(false);
        _isArmed = false;
    }

    public void newSweepData(XYPoint[] data)
    {
        _navigation.newSweep(data);
    }


}





















   /* public void C_data(byte key,short[] data)
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
    }*/
/*
    public void C_message(byte key)
    {

    }*/



   /* */






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