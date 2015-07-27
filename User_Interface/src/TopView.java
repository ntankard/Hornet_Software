import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


/**
 * Created by Matt on 15/07/2015.
 */
public class TopView extends JPanel {
    ArrayList<PointXY> list = new ArrayList<>();

    public TopView() {
        setBackground(Color.red);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); //do what paintComponent does, and i will tell you what to do from now on
        for(int i = list.size(); i > 0; i--) {
            g.fillOval(800 + list.get(i-1).getX(), 800 + list.get(i-1).getY() ,10,10);
            System.out.println("X: " + list.get(i-1).getX() + " Y: " + list.get(i-1).getY());
        }
    }

    public void draw(ArrayList<PointXY> sentList) {
        list = sentList;
        repaint();
    }
}
