package hornet.gui.rootPanels;

import hornet.coms.DataPacket;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

/**
 * Created by Nicholas on 17/09/2015.
 */
public class RP_Coms {
    private JTabbedPane Coms_TPanel;
    private JPanel rootPanel;
    private JPanel Debug_Tab;
    private JPanel DataIn_Tab;
    private JList Debug_List;
    private JScrollPane Debug_Scroll;
    private JScrollPane DataIn_Scroll;
    private JList DataIn_List;
    private JPanel DataOut_Tab;
    private JScrollPane DataOut_Scroll;
    private JList DataOut_List;
    private JCheckBox Hex_checkBox;
    private JTextField Debug_logLengh_text;
    private JTextField DataOut_logLength_text;
    private JTextField DataIn_logLength_text;

    private Vector<String> _debugMessages = new Vector<>();
    private Vector<String> _dataInMessages = new Vector<>();
    private Vector<String> _dataOutMessages = new Vector<>();

    public void addDebugMessage(byte[] message)
    {
        String str;
        if(Hex_checkBox.isSelected())
        {
            str = "";

            for(int i=0;i<message.length;i++)
            {
                int toConvert = 0xff&message[i];
                str+=String.format("0x%2s", Integer.toHexString(toConvert)).replace(' ', '0');
                str+=" ";
            }
        }
        else
        {
            str = new String(message, StandardCharsets.UTF_8);
        }

        // prevent the buffers from exploding
        if(_debugMessages.size() >=Integer.parseInt(Debug_logLengh_text.getText()))
        {
            _debugMessages.remove(0);
        }

        // add the new message
        _debugMessages.add(str);
        Debug_List.setListData(_debugMessages);

        // set the scroll to the bottom
        JScrollBar vertical = Debug_Scroll.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    public void addDataIn(DataPacket data)
    {
        // prevent the buffers from exploding @TODO magic number
        if(_dataInMessages.size() >=Integer.parseInt(DataIn_logLength_text.getText()))
        {
            _dataInMessages.remove(0);
        }

        String toAdd = new String();
        toAdd+= (char)data.getID();

        if(data.length != 1) {
            for (int i = 0; i < (data.getShortPayload().length); i++) {
                String shortString = Short.toString(data.getShortPayload()[i]);
                String rightPadding = "   |";
                String leftPadding = "        ".substring(shortString.length());

                toAdd += leftPadding;
                toAdd += shortString;
                toAdd += rightPadding;
            }
        }
        _dataInMessages.add(String.valueOf(toAdd));
        DataIn_List.setListData(_dataInMessages);

        // set the scroll to the bottom
        JScrollBar vertical = DataIn_Scroll.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    public void addDataOut(DataPacket data)
    {
        // prevent the buffers from exploding @TODO magic number
        if(_dataOutMessages.size() >=Integer.parseInt(DataOut_logLength_text.getText()))
        {
            _dataOutMessages.remove(0);
        }

        String toAdd = new String();
        toAdd+= (char)data.getID();
        if(data.length != 1) {
            for (int i = 0; i < data.getShortPayload().length; i++) {
                String shortString = Short.toString(data.getShortPayload()[i]);
                String rightPadding = "   |";
                String leftPadding = "        ".substring(shortString.length());

                toAdd += leftPadding;
                toAdd += shortString;
                toAdd += rightPadding;
            }
        }
        _dataOutMessages.add(String.valueOf(toAdd));
        DataOut_List.setListData(_dataOutMessages);

        // set the scroll to the bottom
        JScrollBar vertical = DataOut_Scroll.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }
}

