package hornet.gui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;

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
    private JTextField Loops_Text;
    private JTextField Loss_Text;
    private JTextField Sent_Text;
    private JTextField LidarReceived_Text;
    private JTextField LidarLoss_Text;
    private JTextField LidarPer_Text;

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

        Loops_Text.setText("0");
        Loss_Text.setText("0");
    }

    public void setState(int state)
    {
        if(state < 0 || state >8)
        {
            return;
        }
        for(int i=0;i<8;i++)
        {
            _panels[i].setBackground(Color.GRAY);
        }

        _panels[state].setBackground(Color.GREEN);
    }

    public void setLoss(double loss)
    {
        Loss_Text.setText(Double.toString(loss));
    }

    public void setLoopCount(int c)
    {
        Loops_Text.setText(Integer.toString(c));
    }

    public void setSendCount(int c)
    {
        Sent_Text.setText(Integer.toString(c));
    }

    public void setLidar(int r,int l)
    {
        double per = ((double)l/(double)(r+l)) * 100.0;
        LidarLoss_Text.setText(Integer.toString(l));
        LidarReceived_Text.setText(Integer.toString(r));
        LidarPer_Text.setText(Integer.toString((int)per));
    }
}
