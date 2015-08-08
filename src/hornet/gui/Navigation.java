package hornet.gui;

import javax.swing.*;

import net.java.games.input.Controller;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;
import hornet.VirtualHornet;


/**
 * Created by Nicholas on 12/07/2015.
 */
public class Navigation extends JFrame {
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
    private JPanel X;
    private JPanel Y;
    private JPanel Z;

    private ArrayList<Controller> foundControllers;

    private VirtualHornet virtualHornet;

    private Vector<String> sentMessages;
    private Vector<String> receivedMessages;

   // private JInputJoystickTest joy;

    public Navigation(VirtualHornet theVirtualHornet)
    {
        sentMessages = new Vector<>();
        receivedMessages = new Vector<>();

        virtualHornet = theVirtualHornet;
        Connect_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                virtualHornet.UI_connect(SerialPort_Combo.getSelectedItem().toString(), 9600);
                super.mouseClicked(e);
            }
        });

        rootPanel.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                X.setSize((int)X.getSize().getWidth(),(int)X.getSize().getWidth());
                Y.setSize((int)Y.getSize().getWidth(),(int)Y.getSize().getWidth());
                Z.setSize((int)Z.getSize().getWidth(),(int)Z.getSize().getWidth());
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
    }


    public enum ConnectionState{Disconnected, Connecting, Connected}

    public void setConnectionState(ConnectionState state)
    {
        switch(state)
        {
            case Disconnected:
                ConnectionStatus_lbl.setText("Disconnected");
                ConnectionStatus_lbl.setBackground(Color.RED);
                Connect_btn.setText("Connect");
                break;
            case Connecting:
                ConnectionStatus_lbl.setText("Connecting");
                ConnectionStatus_lbl.setBackground(Color.YELLOW);
                Connect_btn.setText("Cancel");
                break;
            case Connected:
                ConnectionStatus_lbl.setText("Connected");
                ConnectionStatus_lbl.setBackground(Color.GREEN);
                Connect_btn.setText("Disconnect");
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
        if(sentMessages.size() >=10)
        {
            sentMessages.remove(0);
        }

        sentMessages.add(message);
        SentMessages_List.setListData(sentMessages);

        JScrollBar vertical = SentMessages_Scroll.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    public void newReceivedMessage(String message)
    {
        if(receivedMessages.size() >=10)
        {
            receivedMessages.remove(0);
        }

        receivedMessages.add(message);
        ReceivedMessages_List.setListData(receivedMessages);

        JScrollBar vertical = ReceivedMessages_Scroll.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());

        Graphics test =  Navigation.getGraphics();
        test.drawLine(0, 0, 100, 100);
    }

    public void accGyro(float[] acc,float[] gyro) {
        AccX_Text.setText(Float.toString(acc[0]));
        AccY_Text.setText(Float.toString(acc[1]));
        AccZ_Text.setText(Float.toString(acc[2]));

        GyroX_Text.setText(Float.toString(gyro[0]));
        GyroY_Text.setText(Float.toString(gyro[1]));
        GyroZ_Text.setText(Float.toString(gyro[2]));
    }

    public void open()
    {

        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        Graphics g = X.getGraphics();

        g.drawOval(X.getWidth()/2,X.getWidth()/2,X.getWidth(),X.getWidth());
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
