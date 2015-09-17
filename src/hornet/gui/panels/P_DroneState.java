package hornet.gui.panels;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Nicholas on 18/09/2015.
 */
public class P_DroneState {
    private JPanel rootPanel;
        private JPanel Init_Panel;
        private JPanel Connect_Panel;
        private JPanel Idle_Panel;
        private JPanel Takeoff_Panel;
        private JPanel Flight_Panel;
        private JPanel Land_Panel;
        private JPanel Emergency_Panel;
        private JPanel Crash_Panel;

    private JPanel[] _panels = new JPanel[8];

    public P_DroneState()
    {
        _panels[0]=Init_Panel;
        _panels[1]=Connect_Panel;
        _panels[2]=Idle_Panel;
        _panels[3]=Takeoff_Panel;
        _panels[4]=Flight_Panel;
        _panels[5]=Land_Panel;
        _panels[6]=Emergency_Panel;
        _panels[7]=Crash_Panel;

        for(int i=0;i<8;i++)
        {
            _panels[i].setBackground(Color.GRAY);
        }
    }

    public void setState(int state)
    {
        for(int i=0;i<8;i++)
        {
            _panels[i].setBackground(Color.GRAY);
        }

        _panels[state].setBackground(Color.GREEN);
    }
}
