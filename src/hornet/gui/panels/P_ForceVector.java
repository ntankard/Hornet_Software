package hornet.gui.panels;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by Nicholas on 27/10/2015.
 */
public class P_ForceVector extends JPanel {

    //private double _angle = 0;
    private int _boarder = 0;

    private double _XComensator =0;
    private double _YCompensator =0;

    public P_ForceVector()
    {}


    public void setCompensationVector(double xPer, double yPer)
    {
        _XComensator = xPer;
        _YCompensator  = yPer;
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
        double x = ((double)r)+(((double)r)*(_XComensator/100.0));
        double y = ((double)r)-(((double)r)*(_YCompensator/100.0));
        g2d.drawLine(r,r,(int)x,(int)y);
    }

    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }
}