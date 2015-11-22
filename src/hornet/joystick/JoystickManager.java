package hornet.joystick;

import hornet.CONFIG;
import hornet.VirtualHornet;
import net.java.games.input.Controller;

/**
 * Created by Nicholas on 11/08/2015.
 */
public class JoystickManager {

    private JoystickMonitor _monitor;
    private Controller _controller;
    private VirtualHornet _virtualHornet;

    public JoystickManager(VirtualHornet theVirtualHornet)
    {
        _virtualHornet = theVirtualHornet;
    }

    public boolean connect(String name)
    {
        disconnect();

        _controller = JoystickUtility.getController(name);
        if(_controller == null)
        {
            return false;
        }

        _monitor = new JoystickMonitor(_virtualHornet,_controller);
        _monitor.start();

        return true;
    }

    public boolean isConnected()
    {
        if(_controller == null)
        {
            return false;
        }
        return true;
    }

    public void disconnect()
    {
        if(_monitor != null) {
            _monitor.interrupt();
            while (_monitor.isAlive()) {
            }
            _controller = null;
        }
    }

    public boolean isSafe()
    {
        if(_monitor != null) {
            return _monitor.isSafe();
        }
        return false;
    }
}

class JoystickMonitor extends Thread {

    private Controller _controller;
    private JoystickInstance _a;
    private JoystickInstance _b;
    private boolean isA;
    private VirtualHornet _virtualHornet;

    JoystickMonitor(VirtualHornet theVirtualHornet, Controller theController)
    {
        _virtualHornet = theVirtualHornet;
        _controller = theController;

        _a = JoystickUtility.generateInstance(_controller);
        _b = JoystickUtility.generateInstance(_controller);   //@TODO verify that this has created 2 identical kinds of instances
        isA = false;

        short[] toSend;

        toSend = new short[2];
        toSend[0] = (short)((_a.getX()));
        toSend[1] = (short)((_a.getY()));

        // filter to prevent noise
        if(toSend[0] > -4 && toSend[0] <4)
        {
            toSend[0] = 0;
        }
        if(toSend[1] > -4 && toSend[1] <4)
        {
            toSend[1] = 0;
        }

        _virtualHornet.newDataOut(CONFIG.Coms.PacketCodes.JOY_XY,toSend);

        toSend = new short[1];
        toSend[0] = (short)(int)(_a.getOtherAxis().get("Slider"));
        _virtualHornet.newDataOut(CONFIG.Coms.PacketCodes.JOY_THROTTLE, toSend);

        toSend = new short[1];
        toSend[0] = (short)(int)(_a.getOtherAxis().get("Z Rotation"));
        _virtualHornet.newDataOut(CONFIG.Coms.PacketCodes.JOY_Z, toSend);
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            read();
            try {
                sleep(10);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    void read()
    {
        JoystickInstance past;
        JoystickInstance current;

        if(isA)
        {
            JoystickUtility.getInstance(_controller,_a);
            past = _b;
            current = _a;
        }
        else
        {
            JoystickUtility.getInstance(_controller,_b);
            past = _a;
            current = _b;
        }

        if(!current.isEqualXY(past))
        {
            short[] toSend = new short[2];
            toSend[0] = (short)((current.getX()/2)+25);
            toSend[1] = (short)((current.getY()/2)+25);

            _virtualHornet.newDataOut(CONFIG.Coms.PacketCodes.JOY_XY, toSend);
        }

        if(!current.isEqualOtherAxis(past, "Slider"))
        {
            short[] toSend = new short[1];
            toSend[0] = (short)(int)(100-current.getOtherAxis().get("Slider"));
            _virtualHornet.newDataOut(CONFIG.Coms.PacketCodes.JOY_THROTTLE, toSend);
            System.out.println(toSend[0]);
        }

        if(!current.isEqualOtherAxis(past, "Z Rotation"))
        {
            short[] toSend = new short[1];
            toSend[0] = (short)((int)(current.getOtherAxis().get("Z Rotation")/2+25));
            _virtualHornet.newDataOut(CONFIG.Coms.PacketCodes.JOY_Z, toSend);
        }

        if(!current.isEqualButton(past, "4"))
        {
            if(current.getButtons().get("4"))
            {
               _virtualHornet.J_arm();
            }
        }

        if(!current.isEqualButton(past, "2"))
        {
            if(current.getButtons().get("2"))
            {
                _virtualHornet.J_disarm();
            }
        }


        if(!current.isEqualButton(past, "3"))
        {
            if(current.getButtons().get("3"))
            {
                _virtualHornet.J_avoidOn();
            }
        }

        if(!current.isEqualButton(past, "1"))
        {
            if(current.getButtons().get("1"))
            {
                _virtualHornet.J_avoidOff();
            }
        }


        isA = !isA;

    }

    public boolean isSafe()
    {
        JoystickInstance current;

        if(isA)
        {
            current = _b;
        }
        else
        {
            current = _a;
        }

        if((100-current.getOtherAxis().get("Slider")) == 0)
        {
            return true;
        }
        return false;
    }
}
