package hornet.coms;

import java.util.Arrays;

/**
 * Created by Nicholas on 8/10/2015.
 */
public class ComPacket {

    private DataPacket _packet;

    private int _sendCount;

    private int _length;

    private int _checksum;

    public ComPacket(DataPacket _packet, int _sendCount) {
        this._packet = _packet;
        this._sendCount = _sendCount;
    }

    public ComPacket(byte[] message)
    {
        if(message.length <= 4)
        {
            return;
        }

        _length = (int)(message[0]&0xFF);
        _sendCount = (int)(message[1]&0xFF);

        _packet = new DataPacket(Arrays.copyOfRange(message, 2, message.length-1));

        _checksum = (int)(message[message.length-1]&0xFF);
    }

    private int getCheckSum()
    {
        int toAdd;
        int check = 0;

        toAdd = ((int)(_length&0xFF));
        check += ((toAdd * (1)) & 0xFF);

        toAdd = ((int)(_sendCount&0xFF));
        check += ((toAdd * (2)) & 0xFF);

        byte[] packetBytes = _packet.getBytes();

        for(int i=0;i<packetBytes.length;i++)
        {
            toAdd = ((int)(packetBytes[i]&0xFF));
            check += ((toAdd * (i+3)) & 0xFF);
        }

        return check & 0xff;
    }

    public  boolean isValid()
    {
        if(_packet.length() != _length)
        {
            return false;
        }
        if(_checksum != getCheckSum())
        {
            return false;
        }
        return true;
    }




}
