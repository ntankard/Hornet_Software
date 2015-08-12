package hornet.joystrick;

import com.digi.xbee.api.RemoteXBeeDevice;
import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.models.XBee64BitAddress;
import com.sun.jmx.snmp.internal.SnmpSubSystem;
import hornet.CONFIG;
import hornet.VirtualHornet;
import net.java.games.input.Controller;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Nicholas on 11/08/2015.
 */
public class JoystickManager {

   // private boolean _isConnected;
    private JoystickMonitor _monitor;
    private Controller _controller;
    private VirtualHornet _virtualHornet;
    //private JoystickManager _manager;

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

    public void disconnect()
    {
        if(_monitor != null) {
            _monitor.interrupt();
            while (_monitor.isAlive()) {
            }
        }
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

        _virtualHornet.J_newXY(_a.getX(), _a.getY());
        _virtualHornet.J_newThrottle(_a.getOtherAxis().get("Slider"));
        _virtualHornet.J_newRotation(_a.getOtherAxis().get("Z Rotation"));
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

        if(current.isEqualXY(past))
        {
            _virtualHornet.J_newXY(current.getX(), current.getY());
        }

        if(current.isEqualOtherAxis(past, "Slider"))
        {
            _virtualHornet.J_newThrottle(current.getOtherAxis().get("Slider"));
        }

        if(current.isEqualOtherAxis(past,"Z Rotation"))
        {
            _virtualHornet.J_newRotation(current.getOtherAxis().get("Z Rotation"));
        }

    }
}
