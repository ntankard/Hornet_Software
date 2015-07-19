import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import java.util.ArrayList;

/**
 * Created by Nicholas on 12/07/2015.
 */
public class Navigation extends JFrame {
    private JPanel rootPanel;
    private JComboBox Controler_Select_Combo;

    private ArrayList<Controller> foundControllers;

   // private JInputJoystickTest joy;

    public Navigation()
    {
        super("Hello world");

        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);



      /*  // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

       // initComponents();

        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
*/
        JoystickManager joy;
        joy = new JoystickManager();

        ArrayList<String> found = joy.getControllersNames();
        for(int i=0;i<found.size();i++)
        {
            Controler_Select_Combo.addItem(found.get(i));
        }


    }


}
