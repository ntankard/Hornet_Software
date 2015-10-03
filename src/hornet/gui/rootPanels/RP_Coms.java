package hornet.gui.rootPanels;

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
    private JPanel Data_Tab;
    private JList Debug_List;
    private JScrollPane Debug_Scroll;
    private JScrollPane Data_Scroll;
    private JList Data_List;

    private Vector<String> _debugMessages = new Vector<>();
    private Vector<String> _dataMessages = new Vector<>();

    public void addDebugMessage(byte[] message)
    {
        String str = new String(message, StandardCharsets.UTF_8);
       /*
        String str = "";

        for(int i=0;i<message.length;i++)
        {
            int toConvert = 0xff&message[i];
            str+=String.format("0x%2s", Integer.toHexString(toConvert)).replace(' ', '0');
            str+=" ";
        }

        str+="C:";
        str+=String.format("0x%2s", Integer.toHexString(getCheckSum(message))).replace(' ', '0');
        */

        //String str String.format("0x%8s", Integer.toHexString(n)).replace(' ', '0');
        // prevent the buffers from exploding @TODO magic number
        if(_debugMessages.size() >=10)
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

    public void addData(byte key, short[] data)
    {
        // prevent the buffers from exploding @TODO magic number
        if(_dataMessages.size() >=10)
        {
            _dataMessages.remove(0);
        }

        String toAdd = new String();
        toAdd+= (char)key;

        for(int i=0;i< data.length;i++)
        {
            String shortString = Short.toString(data[i]);
            String rightPadding = "   |";
            String leftPadding = "        ".substring(shortString.length());

            toAdd += leftPadding;
            toAdd += shortString;
            toAdd += rightPadding;
        }
        _dataMessages.add(String.valueOf(toAdd));
        Data_List.setListData(_dataMessages);

        // set the scroll to the bottom
        JScrollBar vertical = Data_Scroll.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    private int getCheckSum(byte[] message)
    {
        int toAdd;
        int check = 0;
        for(int i=0;i<message.length-1;i++)
        {
            toAdd = ((int)(message[i]&0xFF));
            check += ((toAdd * (i+1)) & 0xFF);
        }

        return check & 0xff;
    }
}
