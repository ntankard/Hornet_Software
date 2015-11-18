package hornet.gui.rootPanels;

import hornet.gui.panels.P_Radar;
import hornet.lidar.XYPoint;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Nicholas on 23/10/2015.
 */
public class RP_LidarTopDown {
    private JPanel rootPanel;
    private JPanel TopDown_Panel;


    private void createUIComponents() {
        // TODO: place custom component creation code here
        TopDown_Panel = new P_Radar();
    }

    public void setPoint(XYPoint[] points)
    {
        ((P_Radar)TopDown_Panel).setPoint(points);
    }

    public void setAnchor(XYPoint point, int i)
    {
        ((P_Radar)TopDown_Panel).setAnchor(point,i);
    }
}
