package hornet.gui;

import javax.swing.*;
import java.util.ArrayList;

import hornet.CONFIG;
import hornet.VirtualHornet;

import hornet.gui.panels.*;
import hornet.gui.rootPanels.RP_ComSettings;
import hornet.gui.rootPanels.RP_Coms;
import hornet.gui.rootPanels.RP_JoyStick;
import hornet.gui.rootPanels.RP_Orientation;


/**
 * Created by Nicholas on 12/07/2015.
 */
public class Navigation  {

    private JFrame _frame;
        private JPanel rootPanel;
            private JPanel Comunications;
                private RP_ComSettings ComSettings_Panel;
            private JTabbedPane Status;
                private RP_Coms Coms_Panel;
                private RP_JoyStick Joystick_Panel;
            private JPanel Navigation;
                private RP_Orientation Gyro_Panel;
            private JPanel DroneState;
                private P_DroneState DroneState_Panel;

    // -----------------------------------------------------------------------------------------------------------------
    // ################################################# SETUP #########################################################
    // -----------------------------------------------------------------------------------------------------------------

    public Navigation(VirtualHornet theVirtualHornet)
    {
        // distribute the virtual hornet
        Joystick_Panel.setVirtualHornet(theVirtualHornet);
        ComSettings_Panel.setVirtualHornet(theVirtualHornet);

    }

    private void createUIComponents() {

    }

    public void open()
    {
        _frame = new JFrame("Hornet");

        _frame.setContentPane(rootPanel);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.pack();
        _frame.setVisible(true);

        _frame.repaint();
    }

    public void start()
    {
        Joystick_Panel.updateJoystickList();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ########################################### STANDARD MESSAGES ###################################################
    // -----------------------------------------------------------------------------------------------------------------

    public void newDebugData(String message)
    {
        Coms_Panel.addDebugMessage(message);
    }

    public void newData(byte key,short[] data)
    {
        Coms_Panel.addData(key, data);

        switch (key)
        {
            case CONFIG.Coms.PacketCodes.GYRO:
                Gyro_Panel.newSettings(data);
                break;
            case CONFIG.Coms.PacketCodes.JOY_THROTTLE:
                Joystick_Panel.updateJoystickThrottle(data[0]);
                break;
            case CONFIG.Coms.PacketCodes.JOY_Z:
                Joystick_Panel.updateJoystickRotation(data[0]);
                break;
            case CONFIG.Coms.PacketCodes.JOY_XY:
                Joystick_Panel.updateJoystickXY(data[0],data[1]);
                break;
            case CONFIG.Coms.PacketCodes.STATUS:
                DroneState_Panel.setState(data[0]);
                break;
        }
        _frame.repaint();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ############################################# SPECIAL EVENTS ####################################################
    // -----------------------------------------------------------------------------------------------------------------

    public void setConnectionState(RP_ComSettings.ConnectionState state)
    {
        ComSettings_Panel.setConnectionState(state);
    }

    public void setComPorts(ArrayList<String> ports)
    {
        ComSettings_Panel.setComPorts(ports);
    }

}





/*
public class Navigation extends JFrame {
    private JPanel rootPanel;
    private JComboBox jComboBox_controllers;
    private JTabbedPane Status;
    private JPanel JoyStick;
    private JPanel jPanelAxes;
    private JLabel jLabelXYAxis;
    private JPanel jPanelXYAxis;
    private JPanel jPanel_forAxis;
    private JPanel jPanelButtons;
    private JPanel jPanelHatSwitch;

    private ArrayList<Controller> foundControllers;

   // private JInputJoystickTest joy;

    public Navigation()
    {
        // load the form
      // /* super("Navigation");
        setContentPane(rootPanel);
        initComponents();
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //  setVisible(true);
initComponents();
this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setVisible(true);


        }

private void initComponents() {

        jPanelAxes = new javax.swing.JPanel();
        jLabelXYAxis = new javax.swing.JLabel();
        jPanelXYAxis = new javax.swing.JPanel();
        jPanel_forAxis = new javax.swing.JPanel();
        jPanelButtons = new javax.swing.JPanel();
        jPanelHatSwitch = new javax.swing.JPanel();
        jComboBox_controllers = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JInput Joystick Test");

        jPanelAxes.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Axes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 51, 204)));

        jLabelXYAxis.setText("X Axis / Y Axis");

        jPanelXYAxis.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelXYAxis.setPreferredSize(new java.awt.Dimension(111, 111));

        javax.swing.GroupLayout jPanelXYAxisLayout = new javax.swing.GroupLayout(jPanelXYAxis);
        jPanelXYAxis.setLayout(jPanelXYAxisLayout);
        jPanelXYAxisLayout.setHorizontalGroup(
        jPanelXYAxisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 109, Short.MAX_VALUE)
        );
        jPanelXYAxisLayout.setVerticalGroup(
        jPanelXYAxisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 109, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel_forAxisLayout = new javax.swing.GroupLayout(jPanel_forAxis);
        jPanel_forAxis.setLayout(jPanel_forAxisLayout);
        jPanel_forAxisLayout.setHorizontalGroup(
        jPanel_forAxisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 202, Short.MAX_VALUE)
        );
        jPanel_forAxisLayout.setVerticalGroup(
        jPanel_forAxisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanelAxesLayout = new javax.swing.GroupLayout(jPanelAxes);
        jPanelAxes.setLayout(jPanelAxesLayout);
        jPanelAxesLayout.setHorizontalGroup(
        jPanelAxesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanelAxesLayout.createSequentialGroup()
        .addGroup(jPanelAxesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanelAxesLayout.createSequentialGroup()
        .addGap(58, 58, 58)
        .addComponent(jLabelXYAxis))
        .addGroup(jPanelAxesLayout.createSequentialGroup()
        .addGap(37, 37, 37)
        .addComponent(jPanelXYAxis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jPanel_forAxis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
        );
        jPanelAxesLayout.setVerticalGroup(
        jPanelAxesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanelAxesLayout.createSequentialGroup()
        .addComponent(jLabelXYAxis)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanelXYAxis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(0, 16, Short.MAX_VALUE))
        .addComponent(jPanel_forAxis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanelButtons.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Buttons", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 51, 204)));

        javax.swing.GroupLayout jPanelButtonsLayout = new javax.swing.GroupLayout(jPanelButtons);
        jPanelButtons.setLayout(jPanelButtonsLayout);
        jPanelButtonsLayout.setHorizontalGroup(
        jPanelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 248, Short.MAX_VALUE)
        );
        jPanelButtonsLayout.setVerticalGroup(
        jPanelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 112, Short.MAX_VALUE)
        );

        jPanelHatSwitch.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hat Switch", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 51, 204)));

        javax.swing.GroupLayout jPanelHatSwitchLayout = new javax.swing.GroupLayout(jPanelHatSwitch);
        jPanelHatSwitch.setLayout(jPanelHatSwitchLayout);
        jPanelHatSwitchLayout.setHorizontalGroup(
        jPanelHatSwitchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 121, Short.MAX_VALUE)
        );
        jPanelHatSwitchLayout.setVerticalGroup(
        jPanelHatSwitchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 0, Short.MAX_VALUE)
        );



        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
        .addComponent(jPanelButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanelHatSwitch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addComponent(jPanelAxes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addGap(0, 0, Short.MAX_VALUE)
        .addComponent(jComboBox_controllers, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(88, 88, 88))))
        );
        layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jComboBox_controllers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jPanelAxes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
        .addComponent(jPanelButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jPanelHatSwitch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
        );

        }

public void addControler(String con)
        {
        jComboBox_controllers.addItem(con);
        }

public Controller getSelectedController()
        {
        // Currently selected controller.
        int selectedControllerIndex = jComboBox_controllers.getSelectedIndex();
        Controller controller = foundControllers.get(selectedControllerIndex);

        return controller;

        }



public void status_updateController(JoystickInstance joy)
        {
        Graphics2D g2d = (Graphics2D)jPanelXYAxis.getGraphics();
        g2d.clearRect(1, 1, jPanelXYAxis.getWidth() - 2, jPanelXYAxis.getHeight() - 2);
        g2d.fillOval(joy.xAxisPercentage, joy.yAxisPercentage, 10, 10);
        }



        }
 */


/*
    public void updateAltitude(int a) {
        ((UltrasonicUI)Altitude_Panel).setDistance(a);
        _frame.repaint();
    }
 */



/*
  public void newSentMessage(String message)
    {
        if(_sentMessages.size() >=10)
        {
            _sentMessages.remove(0);
        }

        _sentMessages.add(message);
        SentMessages_List.setListData(_sentMessages);

        JScrollBar vertical = SentMessages_Scroll.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    public void newReceivedMessage(String message)
    {
        if(_receivedMessages.size() >=10)
        {
            _receivedMessages.remove(0);
        }

        _receivedMessages.add(message);
        ReceivedMessages_List.setListData(_receivedMessages);

        JScrollBar vertical = ReceivedMessages_Scroll.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());

        //Graphics test =  Navigation.getGraphics();
        //test.drawLine(0, 0, 100, 100);
    }

    public void accGyro(short[] acc,short[] gyro) {



        //((P_OrientationIndicator)Z).setAngle(Math.toDegrees(Math.asin(acc[2]/20)));

        //Z.setSize(frame.getWidth() / 3, frame.getWidth() / 3);
        // X.setSize(frame.getWidth()/3,frame.getWidth()/3);
        // X.setSize(frame.getWidth()/3,frame.getWidth()/3);

        //frame.getWidth()/3;

        //((P_OrientationIndicator)Z).setSize(((P_OrientationIndicator)Z).getWidth(),((P_OrientationIndicator)Z).getWidth());
        _frame.repaint();
    }
 */


/*
    public void updateJoystickList()
    {
        Joystick_Panel.updateJoystickList();
    }

    public void updateJoystickXY(int x,int y) {
        Joystick_Panel.updateJoystickXY(x, y);
        _frame.repaint();
    }

    public void updateJoystickRotation(int r)
    {
        Joystick_Panel.updateJoystickRotation(r);
        _frame.repaint();
    }

    public void updateJoystickThrottle(int r)
    {
        Joystick_Panel.updateJoystickThrottle(r);
        _frame.repaint();
    }

 */