package hornet.gui.panels;

import hornet.lidar.SweepPoint;
import hornet.lidar.XYPoint;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Nicholas on 23/10/2015.
 */
public class P_Radar extends JPanel {

    XYPoint[] _points;

    XYPoint[] _anchors = new XYPoint[4];

    public P_Radar()
    {
        for(int i=0;i<_anchors.length;i++)
        {
            SweepPoint toAdd = new SweepPoint(0,0);
            _anchors[i] = new XYPoint(toAdd,6000,0);
        }
    }

    public void setPoint(XYPoint[] points)
    {
        _points = points;
    }

    public void setAnchor(XYPoint point, int i)
    {
        _anchors[i] = point;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;


        // find the shortest side to keep a perfect circle
        int r;
        if(getWidth()<getHeight())
        {
            r = (getWidth()/2);
        }
        else
        {
            r = (getHeight()/2);
        }

        int step = r/6;

        // draw the distance indicators
        for(int i=1;i<6;i++)
        {

            g2d.drawOval(step*i,step*i,r*2 - ((step*i)*2),r*2 - ((step*i)*2));
        }

        if(_points != null)
        {
            g2d.setColor(Color.RED);
            for(int i=0;i<_points.length;i++)
            {
                int x = (int)((double)r*2.0*(_points[i].get_xP()/100.0f));
                int y = (int)((double)r*2.0*(_points[i].get_yP()/100.0f));

              //  int shift = y-r;
             //   y=r-shift;

                int shift = x-r;
                x=r-shift;

                g2d.fillOval(x-2,y-2,4,4);
            }
        }

        // draw the a
        g2d.setColor(Color.GREEN);
        for(int i=0;i<_anchors.length;i++)
        {
            int x = (int)((double)r*2.0*(_anchors[i].get_xP()/100.0f));
            int y = (int)((double)r*2.0*(_anchors[i].get_yP()/100.0f));

            //  int shift = y-r;
            //   y=r-shift;

            int shift = x-r;
          // // x=r-shift;

            g2d.drawLine(x,y,r,r);

           // g2d.fillOval(x-2,y-2,4,4);
        }

      //  g2d.drawOval(0,0,r*2,r*2);
        //g2d.drawOval(r/2,r/2,r+(r/2),r+(r/2));
    }

    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }

}
