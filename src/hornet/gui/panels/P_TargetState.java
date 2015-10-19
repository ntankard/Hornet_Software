package hornet.gui.panels;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Nicholas on 15/10/2015.
 */
public class P_TargetState {
    private JPanel rootPanel;
        private JPanel Disarm_Panel;
        private JPanel Arm_Panel;

    public void P_TargetState()
    {
        Disarm_Panel.setBackground(Color.GRAY);
        Arm_Panel.setBackground(Color.GRAY);
    }

    public void setIsArmed(boolean isArmed)
    {
        if(isArmed)
        {
            Disarm_Panel.setBackground(Color.GRAY);
            Arm_Panel.setBackground(Color.GREEN);
        }
        else
        {
            Disarm_Panel.setBackground(Color.GREEN);
            Arm_Panel.setBackground(Color.GRAY);
        }
    }
}
