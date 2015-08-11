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
    private JoystickInstance _past;
    private VirtualHornet _virtualHornet;

    JoystickMonitor(VirtualHornet theVirtualHornet, Controller theController)
    {
        _virtualHornet = theVirtualHornet;
        _controller = theController;
        _past = JoystickUtility.getInstance(_controller);
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
        JoystickInstance current = JoystickUtility.getInstance(_controller);

        if(current.xAxisPercentage != _past.xAxisPercentage || current.yAxisPercentage != _past.yAxisPercentage)
        {
            _virtualHornet.J_newXY(current.xAxisPercentage, current.yAxisPercentage);
        }

        if(current.otherAxis.get("Slider") != _past.otherAxis.get("Slider"))
        {
            _virtualHornet.J_newThrottle(current.otherAxis.get("Slider"));
        }

        if(current.otherAxis.get("Z Rotation") != _past.otherAxis.get("Z Rotation"))
        {
            _virtualHornet.J_newRotation(current.otherAxis.get("Z Rotation"));
        }



        _past = current;
    }
}
