import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FrontView extends JPanel{
    ArrayList<Point3D> list = new ArrayList<>();

    public FrontView() {
        setBackground(Color.black);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); //do what paintComponent does, and i will tell you what to do from now on
        for(int i = list.size(); i > 0; i--) {
            g.setColor(calculateColor(list.get(i-1).getDistance()));
            g.fillOval(list.get(i-1).getYaw(),list.get(i-1).getPitch() ,10,10);
        }
    }

    public Color calculateColor(int distance) {
        Color color;
        if (distance < 100) {color = Color.red;}
        else if (distance < 150) {color = Color.orange;}
        else if (distance < 250) {color = Color.yellow;}
        else {color = Color.green;}
        return color;
    }

    public void draw(ArrayList<Point3D> sentList) {
        list = sentList;
        repaint();
    }
}