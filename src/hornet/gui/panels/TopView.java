package hornet.gui.panels;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by Matt on 15/07/2015.
 */
public class TopView extends JPanel {
    ArrayList<PointXY> drawablePoints = new ArrayList<>();

    private int CANVAS_HEIGHT = 600;
    private int CANVAS_WIDTH = 600;

    public TopView() {
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        setBackground(Color.white);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); //do what paintComponent does, and i will tell you what to do from now on
        for(int i = drawablePoints.size(); i > 0; i--) {
            drawablePoints.get(i-1).calculatePoint(CANVAS_WIDTH, CANVAS_HEIGHT);
            g.fillOval(drawablePoints.get(i-1).getX(), drawablePoints.get(i-1).getY() ,10,10);
        }
    }

    public void draw() {
        readData();
        repaint();
    }

    public void readData() {
        //THIS WORKS
        // The name of the file to open.
        String fileName = "LIDAR-Data.txt";

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
                PointXY point = new PointXY(line);
                drawablePoints.add(point);
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
