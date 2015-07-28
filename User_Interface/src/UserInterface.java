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
public class UserInterface {

    public static void main(String [] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static void addComponentsToPane(Container pane) {
        //Buttons
        JButton left = new JButton("Left");
        JButton forward = new JButton("Forward");
        JButton right = new JButton("Right");
        //FrontView
        FrontView viewPanel = new FrontView();
        viewPanel.draw();

        JPanel boxPanel = new JPanel();
        boxPanel.setBorder(new TitledBorder("Button Panel"));
        boxPanel.setOpaque(false);
        boxPanel.add(left);
        boxPanel.add(forward);
        boxPanel.add(right);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(viewPanel, BorderLayout.CENTER);
        centerPanel.add(boxPanel, BorderLayout.SOUTH);
        centerPanel.setBorder(new TitledBorder("Center Panel"));
        centerPanel.setOpaque(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.add(centerPanel);
        mainPanel.setBorder(new TitledBorder("Main Panel"));
        mainPanel.setBackground(new Color(216, 252, 202));

        pane.add(mainPanel);
        //TopView
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Use Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addComponentsToPane(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }
}
