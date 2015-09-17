package hornet.gui.rootPanels;

import hornet.gui.panels.P_OrientationIndicator;

import javax.swing.*;

/**
 * Created by Nicholas on 17/09/2015.
 */
public class RP_Orientation {
    private JPanel rootPanel;
    private JPanel Yaw_Panel;
    private JPanel Pitch_Panel;
    private JPanel Roll_Panel;

    private void createUIComponents() {
        // TODO: place custom component creation code here
        Yaw_Panel = new P_OrientationIndicator("Yaw");
        Pitch_Panel = new P_OrientationIndicator("Pitch");
        Roll_Panel = new P_OrientationIndicator("Roll");
    }

    public void newSettings(short[] gyro)
    {
        double x = (double)gyro[0]/10000.0;
        double y = (double)gyro[1]/10000.0;
        double z = (double)gyro[2]/10000.0;
        ((P_OrientationIndicator) Yaw_Panel).setAngle(x);
        ((P_OrientationIndicator)Pitch_Panel).setAngle(y);
        ((P_OrientationIndicator)Roll_Panel).setAngle(z);
    }
}
