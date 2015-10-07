package hornet;

import hornet.coms.*;
import hornet.gui.Navigation;
import hornet.joystrick.JoystickManager;

/**
 * Created by Nicholas on 19/07/2015.
 */
public class Main {

    public static void main(String args[]) {
        VirtualHornet virtualHornet = new VirtualHornet();

        // create the UI
        Navigation navigation = new Navigation(virtualHornet);
        virtualHornet.attachNavigation(navigation);

        // setup the com systems
        ComsDecoder comsDecoder = new ComsDecoder(virtualHornet);
        //Coms coms = new USBSerial(comsDecoder);
        //Coms coms = new XBee_Series1(comsDecoder);
        Coms coms = new Coms(comsDecoder);

        ComsEncoder comsEncoder = new ComsEncoder(coms,navigation);
        virtualHornet.attachComsEncoder(comsEncoder);
        virtualHornet.attachComs(coms);

        // create the Joystick
        JoystickManager joystickManager = new JoystickManager(virtualHornet);
        virtualHornet.attachJoystickManager(joystickManager);

        // setup all systems
        virtualHornet.start();
    }
}

        /*JoystickManager joy;
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
        }*/





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


