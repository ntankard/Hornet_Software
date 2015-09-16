package hornet.gui;

import javax.swing.*;

import hornet.gui.panels.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;
import hornet.VirtualHornet;
import hornet.joystrick.JoystickUtility;
import hornet.lidar.XYPoint;
//import hornet.gui.panels.

/**
 * Created by Nicholas on 12/07/2015.
 */
public class Navigation  {
    private JPanel rootPanel;
    private JPanel Comunications;
    private JComboBox SerialPort_Combo;
    private JButton Connect_btn;
    private JLabel ConnectionStatus_lbl;
    private JTabbedPane Status;
    private JList<String> SentMessages_List;
    private JList ReceivedMessages_List;
    private JScrollPane SentMessages_Scroll;
    private JScrollPane ReceivedMessages_Scroll;
    private JPanel Navigation;
    private JPanel Orientation;
    private JTextField AccX_Text;
    private JTextField GyroX_Text;
    private JTextField AccY_Text;
    private JTextField GyroY_Text;
    private JTextField AccZ_Text;
    private JTextField GyroZ_Text;
    private JPanel RawValues;
    private JPanel Indicators;
    private JComboBox Joystick_Combo;
    private JPanel Joystick_Panel;
    private JProgressBar Rotation_Bar;
    private JProgressBar Throttle_Bar;
    private JPanel Altitude_Panel;
    private JPanel CriticalStatus;
    private JPanel CommectionStatus;
    private JPanel JoyStickConnected;
    private JPanel JoyStickReady;
    private JPanel Roll;
    private JPanel Yaw;
    private JPanel Pitch;
    private JPanel TopView;


    // private ArrayList<Controller> foundControllers;

    private VirtualHornet _virtualHornet;
    private JFrame _frame;
    private Vector<String> _sentMessages;
    private Vector<String> _receivedMessages;


    // private JInputJoystickTest joy;

    // -----------------------------------------------------------------------------------------------------------------
    // ################################################# SETUP #########################################################
    // -----------------------------------------------------------------------------------------------------------------

    public Navigation(VirtualHornet theVirtualHornet)
    {
        _sentMessages = new Vector<>();
        _receivedMessages = new Vector<>();

        _virtualHornet = theVirtualHornet;

        Connect_btn.addMouseListener(new Connect_btn_click());

        setConnectionState(ConnectionState.Disconnected);
    }

    private void createUIComponents() {
        Roll = new OrientationIndicator("Roll");
        Yaw = new OrientationIndicator("Yaw");
        Pitch = new OrientationIndicator("Pitch");
        Joystick_Panel = new JoystickPos();
        Altitude_Panel = new UltrasonicUI();
        CommectionStatus = new Indicator("Coms");
        JoyStickConnected = new Indicator("JS Connect");
        JoyStickReady = new Indicator("JS Ready");
    }

    public void open()
    {
        _frame = new JFrame("Hornet");
        // Navigation n = new Navigation();
        _frame.setContentPane(rootPanel);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.pack();
        _frame.setVisible(true);

        _frame.repaint();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ############################################### UI EVENT ########################################################
    // -----------------------------------------------------------------------------------------------------------------

    class Connect_btn_click extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e) {
            _virtualHornet.UI_connect(SerialPort_Combo.getSelectedItem().toString(), 9600);
            super.mouseClicked(e);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ############################################ EXTERNAL EVENTS ####################################################
    // -----------------------------------------------------------------------------------------------------------------


    public enum ConnectionState{Disconnected, Connecting, Connected}

    public void setConnectionState(ConnectionState state)
    {
        switch(state)
        {
            case Disconnected:
                ConnectionStatus_lbl.setText("Disconnected");
                ConnectionStatus_lbl.setBackground(Color.RED);
                Connect_btn.setText("Connect");
                ((Indicator)CommectionStatus).turnOff();
                break;
            case Connecting:
                ConnectionStatus_lbl.setText("Connecting");
                ConnectionStatus_lbl.setBackground(Color.YELLOW);
                Connect_btn.setText("Cancel");
                ((Indicator)CommectionStatus).turnOff();
                break;
            case Connected:
                ConnectionStatus_lbl.setText("Connected");
                ConnectionStatus_lbl.setBackground(Color.GREEN);
                Connect_btn.setText("Disconnect");
                ((Indicator)CommectionStatus).turnOn();
                break;
        }
    }

    public void setComPorts(ArrayList<String> ports)
    {
        //@todo erase original content
        for(int i=0;i<ports.size();i++)
        {
            SerialPort_Combo.addItem(ports.get(i));
        }
    }

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

        AccX_Text.setText(Float.toString(acc[0]));
        AccY_Text.setText(Float.toString(acc[1]));
        AccZ_Text.setText(Float.toString(acc[2]));

        GyroX_Text.setText(Float.toString(gyro[0]));
        GyroY_Text.setText(Float.toString(gyro[1]));
        GyroZ_Text.setText(Float.toString(gyro[2]));

        //((OrientationIndicator)Z).setAngle(Math.toDegrees(Math.asin(acc[2]/20)));

        //Z.setSize(frame.getWidth() / 3, frame.getWidth() / 3);
        // X.setSize(frame.getWidth()/3,frame.getWidth()/3);
        // X.setSize(frame.getWidth()/3,frame.getWidth()/3);

        //frame.getWidth()/3;

        //((OrientationIndicator)Z).setSize(((OrientationIndicator)Z).getWidth(),((OrientationIndicator)Z).getWidth());
        _frame.repaint();
    }

    public void gyro(short[] gyro)
    {
        double x = (double)gyro[0]/10000.0;
        double y = (double)gyro[1]/10000.0;
        double z = (double)gyro[2]/10000.0;
        ((OrientationIndicator) Yaw).setAngle(x);
        ((OrientationIndicator)Pitch).setAngle(y);
        ((OrientationIndicator)Roll).setAngle(z);
        _frame.repaint();
    }

    public void pitchRoll(float pitch,float roll) {

        double degPitch = (double)pitch;
        double degRoll = (double)roll;
        ((OrientationIndicator) Yaw).setAngle(degRoll);
        ((OrientationIndicator)Pitch).setAngle(degPitch);

    }

    public void updateJoystickList()
    {
        ArrayList<String> joy = JoystickUtility.getControllersNames();

        if(joy.size() == 0)
        {
            return;
        }

        //@todo erase original content
        for(int i=0;i<joy.size();i++)
        {
            Joystick_Combo.addItem(joy.get(i));
        }

        Joystick_Combo.setSelectedIndex(0);

        _virtualHornet.UI_joystickConnected((String) Joystick_Combo.getSelectedItem());
    }

    public void updateJoystickXY(int x,int y) {
        ((JoystickPos)Joystick_Panel).setPos(x, y);
        _frame.repaint();
    }

    public void updateJoystickRotation(int r)
    {
        Rotation_Bar.setValue(r);
    }

    public void updateJoystickThrottle(int r)
    {
        Throttle_Bar.setValue(r);
    }

    public void updateAltitude(int a) {
        ((UltrasonicUI)Altitude_Panel).setDistance(a);
        _frame.repaint();
    }

    public void turnJoyStickConnectedOn()
    {
        ((Indicator)JoyStickConnected).turnOn();
        _frame.repaint();
    }

    public void turnJoyStickReady()
    {
        ((Indicator)JoyStickReady).turnOn();
        _frame.repaint();
    }

   /* public void updateLidarTopView(ArrayList<XYPoint> sweepPoints)
    {
        ((LidarTopViewUI)TopView).plotPoint(sweepPoints);
        _frame.repaint();
    }*/

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
