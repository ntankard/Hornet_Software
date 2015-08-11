package hornet.gui.panels;

import javax.swing.*;
import java.awt.*;

public class JoystickPos extends JPanel {

    private double _angle = 0;
    private int _boarder = 10;

    private int _xPos;
    private int _yPos;

    public void setPos(int x, int y)
    {
        _xPos=x;
        _yPos=y;
    }

    public void setAngle(double theAngle)
    {
        _angle = Math.toRadians(theAngle+90);
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

        int center = r +_boarder;
        double x = center + (double)(r)*((double)(_xPos-50))/50;
        double y = center + (double)(r)*((double)(_yPos-50))/50;

        g2d.drawOval((int)x-5,(int)y-5,10,10);

        //get the line info



       // g2d.drawLine(center-(int)x,center-(int)y,center+(int)x,center+(int)y);
    }

    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }
}