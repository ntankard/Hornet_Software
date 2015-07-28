import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Matt on 15/07/2015.
 */
public class UserInterface implements ActionListener {

    private JButton left;
    private JButton forward;
    private JButton right;

    public static void main(String [] args) {
        UserInterface u = new UserInterface();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                u.createAndShowGUI();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Left":
                System.out.println("Move Left");
                break;
            case "Right":
                System.out.println("Move Right");
                break;
            case "Forward":
                System.out.println("Move Forward");
        }
    }

    public void addComponentsToPane(Container pane) {
        //Buttons
        left = new JButton("Left");
        forward = new JButton("Forward");
        right = new JButton("Right");
        left.addActionListener(this);
        forward.addActionListener(this);
        right.addActionListener(this);

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

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Use Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addComponentsToPane(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }
}
