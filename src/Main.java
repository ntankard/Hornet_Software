import net.java.games.input.Controller;

import javax.naming.ldap.Control;
import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Nicholas on 19/07/2015.
 */
public class Main {

    public static void main(String args[]) {

        final Navigation nav = new Navigation();

        JoystickManager joy;
        joy = new JoystickManager();

        ArrayList<String> found = joy.getControllersNames();
        for(int i=0;i<found.size();i++)
        {
            nav.addControler(found.get(i));
        }

        joy.updateControllerList();

        while(true)
        {
            Controller con = nav.getSelectedController();
            joy.getInstance(con);
            nav.status_updateController(joy.getInstance(con));
            try {
                Thread.sleep(25);
            } catch (InterruptedException ex) {
            }
        }





       // Navigation dialog = new Navigation();
        //dialog.pack();
      //  dialog.setVisible(true);
       // System.exit(0);

       // JFrameWindow  window = new JFrameWindow();
       
        //JFrame frame = new JFrame("<class name>");
       // frame.setContentPane(new Navigation().);
       // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // frame.pack();
       // frame.setVisible(true);



       // Navigation nav = new Navigation();

     //   while(true)
      //  {

      //  }
    }
}
