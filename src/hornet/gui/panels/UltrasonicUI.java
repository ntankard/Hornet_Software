package hornet.gui.panels;

/**
 * Created by Angela on 12/08/2015.
 */
    import javax.swing.*;
    import java.awt.*;
    import javax.swing.border.TitledBorder;

    public class UltrasonicUI extends JPanel {

        // instance variables
        int distIndicator = 0;

        public UltrasonicUI() {
            setPreferredSize(new Dimension(500, 500));
            setBorder(new TitledBorder("Ultrasonic"));
            setBackground(Color.white);
        }

        public void paintComponent(Graphics g) {
            int x = getWidth() / 2;
            int y = getHeight() / 2;
            int width = 50;
            int height = 400;

            super.paintComponent(g);
            g.drawRect(x - width / 2, y - height / 2, width, height);
            g.drawString("0cm", x - width / 2 + 60, 450);
            g.drawString("200cm", x - width / 2 + 60, 250);
            g.drawString("400cm", x - width / 2 + 60, 50);

            g.setColor(Color.blue);
            g.fillRect(x - width / 2, 450-distIndicator, width, distIndicator);

            repaint();
        }

        public void setDistance(int dist){
            distIndicator = dist;
        }

    }

}