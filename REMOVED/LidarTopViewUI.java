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

    private int CANVAS_HEIGHT = 500;
    private int CANVAS_WIDTH = 500;
    private ArrayList<XYPoint> drawablePoints = new ArrayList<>();

    public LidarTopViewUI() {
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        setBorder(new TitledBorder("Lidar Top View"));
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        for(int i = 0; i<drawablePoints.size(); i++) {
            g.fillOval((int) ((drawablePoints.get(i).get_xP()/100)*CANVAS_WIDTH), (int) ((drawablePoints.get(i).get_yP()/100)*CANVAS_HEIGHT), 10, 10);
        }

    }

    public void plotPoint(ArrayList<XYPoint> points)
    {
        drawablePoints = points;
    }
}