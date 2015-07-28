import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FrontView extends JPanel{
    ArrayList<Point3D> drawablePoints = new ArrayList<>();
    private int CANVAS_WIDTH = 800;
    private int CANVAS_HEIGHT = 800;

    public FrontView() {
        setBackground(Color.black);
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); //do what paintComponent does, and i will tell you what to do from now on
        for(int i = drawablePoints.size(); i > 0; i--) {
            g.setColor(calculateColor(drawablePoints.get(i - 1).getDistance()));
            g.fillOval(drawablePoints.get(i-1).getYaw(),drawablePoints.get(i-1).getPitch() ,10,10);
            System.out.println("Yaw: " + drawablePoints.get(i-1).getYaw() + " Distance: " + drawablePoints.get(i-1).getDistance() + " Pitch: " + drawablePoints.get(i-1).getPitch());
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

    public void draw() {
        readData();
        repaint();
    }

    public void readData() {
        //THIS WORKS
        // The name of the file to open.
        String fileName = "test2.txt";

        // This will reference one line at a time
        String line = null;

        //testing

        //testing

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                //reads 1 ine at a time then moves to the next
                drawablePoints.add(new Point3D(line));
            }
            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
    }
}