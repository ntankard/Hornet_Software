package hornet.gui.panels;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class OrientationIndicator extends JPanel {




    private double _angle = 0;
    private int _boarder = 10;

    public OrientationIndicator(String name)
    {
        setBorder(new TitledBorder(name));
    }

    public void setAngle(double theAngle)
    {
        _angle = theAngle + (Math.PI/2);//Math.toRadians(theAngle+90);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        // find the shortest side to keep a perfect circle
        int r;
        if(getWidth()<getHeight())
        {
            r = (getWidth()/2)-_boarder;
        }
        else
        {
            r = (getHeight()/2)-_boarder;
        }

        // draw the outer indicator
        g2d.drawOval(_boarder,_boarder,r*2,r*2);

        //get the line info
        double x = (r*Math.sin(_angle));
        double y = r*Math.cos(_angle);
        int center = r +_boarder;

        g2d.drawLine(center-(int)x,center-(int)y,center+(int)x,center+(int)y);
    }

    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }
}