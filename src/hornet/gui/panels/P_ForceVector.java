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

    private double _XJoy =0;
    private double _YJoy =0;

    private double _XTotal =0;
    private double _YTotal =0;

    public P_ForceVector()
    {}


    public void setCompensationVector(double xPer, double yPer)
    {
        _XComensator = (xPer - 50.0)*2.0;
        _YCompensator  = (yPer - 50.0)*2.0;
    }

    public void setJoyVector(double xPer, double yPer)
    {
        _XJoy = (xPer - 50.0)*2.0;
        _YJoy  = (yPer - 50.0)*2.0;
    }

    public void setTotalVector(double xPer, double yPer)
    {
        _XTotal = (xPer - 50.0)*2.0;
        _YTotal  = (yPer - 50.0)*2.0;
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
        g2d.setColor(Color.RED);
        double x = ((double)r)+(((double)r)*(_XComensator/100.0));
        double y = ((double)r)+ (((double)r)*(_YCompensator/100.0));
        g2d.drawLine(r,r,(int)x,(int)y);

        g2d.setColor(Color.BLUE);
        x = ((double)r)+(((double)r)*(_XJoy/100.0));
        y = ((double)r)+ (((double)r)*(_YJoy/100.0));
        g2d.drawLine(r,r,(int)x,(int)y);

        g2d.setColor(Color.GREEN);
        x = ((double)r)+(((double)r)*(_XTotal/100.0));
        y = ((double)r)+ (((double)r)*(_YTotal/100.0));
        g2d.drawLine(r,r,(int)x,(int)y);
    }

    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }
}