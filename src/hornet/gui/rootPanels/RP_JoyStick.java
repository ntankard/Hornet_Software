package hornet.gui.rootPanels;

import hornet.VirtualHornet;
import hornet.gui.panels.P_JoystickPos;
import hornet.joystick.JoystickUtility;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Nicholas on 17/09/2015.
 */
public class RP_JoyStick {
    private JPanel roolPanel;
    private JPanel Joystick_Panel;
    private JComboBox Joystick_Combo;
    private JProgressBar Rotation_Bar;
    private JProgressBar Throttle_Bar;

    private VirtualHornet _virtualHornet;

    public void setVirtualHornet(VirtualHornet theVirtualHornet)
    {
        _virtualHornet = theVirtualHornet;
    }

    private void createUIComponents() {
        Joystick_Panel = new P_JoystickPos();
    }

    public void updateJoystickList()
    {
        ArrayList<String> joy = JoystickUtility.getControllersNames();

        if(joy.size() == 0)
        {
            return;
        }

        //@todo erase original content
        for(int i=0;i<joy.size();i++)
        {
            Joystick_Combo.addItem(joy.get(i));
        }

        Joystick_Combo.setSelectedIndex(0);

        _virtualHornet.UI_joystickConnected((String) Joystick_Combo.getSelectedItem());
    }

    public void updateJoystickXY(int x,int y) {
        ((P_JoystickPos)Joystick_Panel).setPos(x, y);
    }

    public void updateJoystickRotation(int r)
    {
        Rotation_Bar.setValue(r);
    }

    public void updateJoystickThrottle(int r)
    {
        Throttle_Bar.setValue(r);
    }
}
