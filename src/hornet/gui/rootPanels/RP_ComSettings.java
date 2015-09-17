package hornet.gui.rootPanels;

import hornet.VirtualHornet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by Nicholas on 17/09/2015.
 */
public class RP_ComSettings {

    private JPanel rootPanel;
        private JComboBox SerialPort_Combo;
        private JButton Connect_Btn;
        private JLabel ConnectionStatus_lbl;
        private JButton Refresh_Btn;

    private VirtualHornet _virtualHornet;

    private ConnectionState _state = ConnectionState.Disconnected;

    public RP_ComSettings() {
        Connect_Btn.addMouseListener(new Connect_Btn_click());
        Refresh_Btn.addMouseListener(new Refresh_Btn_click());
        setConnectionState(ConnectionState.Disconnected);
    }

    public void setVirtualHornet(VirtualHornet theVirtualHornet)
    {
        _virtualHornet = theVirtualHornet;
    }

    class Connect_Btn_click extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e) {
            switch (_state)
            {
                case Disconnected:
                    _virtualHornet.UI_connect(SerialPort_Combo.getSelectedItem().toString(), 9600);
                    break;
                case Connecting:
                    _virtualHornet.UI_disconnect();
                    break;
                case Connected:
                    _virtualHornet.UI_disconnect();
                    break;
            }
            super.mouseClicked(e);
        }
    }

    class Refresh_Btn_click extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e) {
            _virtualHornet.UI_refreshSerialPort();
            super.mouseClicked(e);
        }
    }

    public enum ConnectionState{Disconnected, Connecting, Connected}
    public void setConnectionState(ConnectionState state)
    {
        switch(state)
        {
            case Disconnected:
                ConnectionStatus_lbl.setText("Disconnected");
                ConnectionStatus_lbl.setBackground(Color.RED);
                Connect_Btn.setText("Connect");
                Refresh_Btn.setEnabled(true);
                _state = ConnectionState.Disconnected;
                break;
            case Connecting:
                ConnectionStatus_lbl.setText("Connecting");
                ConnectionStatus_lbl.setBackground(Color.YELLOW);
                Connect_Btn.setText("Cancel");
                Refresh_Btn.setEnabled(false);
                _state =ConnectionState.Connecting;
                break;
            case Connected:
                ConnectionStatus_lbl.setText("Connected");
                ConnectionStatus_lbl.setBackground(Color.GREEN);
                Connect_Btn.setText("Disconnect");
                Refresh_Btn.setEnabled(false);
                _state =ConnectionState.Connected;
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
        SerialPort_Combo.setSelectedIndex(ports.size()-1);
    }
}
