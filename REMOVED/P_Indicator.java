package hornet.gui.panels;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Nicholas on 14/08/2015.
 */
public class P_Indicator extends JPanel {

    private String _message;
    private boolean _isEnabled;

    public P_Indicator(String message)
    {
        _message = message;
        _isEnabled = false;
    }

    public void turnOn()
    {
        _isEnabled = true;
    }

    public void turnOff()
    {
        _isEnabled = false;
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;


        if(_isEnabled) {
            g.setColor(Color.GREEN);
        }else
        {
            g.setColor(Color.RED);
        }

        g.fillRect(5, 5, 90, 20);
        g.setColor(Color.BLACK);
        g.drawRect(5, 5, 90, 20);
        g.drawString(_message, 20, 20);

    }

    public Dimension getPreferredSize() {
        return new Dimension(25, 100);
    }
}
