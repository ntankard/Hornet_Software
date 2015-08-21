package hornet.gui.panels;

import hornet.lidar.XYPoint;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Angela on 21/08/2015.
 */
public class LidarTopViewUI extends JPanel {

    private ArrayList<XYPoint> drawablePoints = new ArrayList<>();

    public LidarTopViewUI() {
        setPreferredSize(new Dimension(500, 500));
        setBorder(new TitledBorder("Lidar Top View"));
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        for(int i = 0; i<drawablePoints.size(); i++) {

        // @TODO
        }

    }

}
