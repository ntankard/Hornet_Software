package hornet.coms;

import java.util.Arrays;

/**
 * Created by Nicholas on 8/10/2015.
 */
public class ComPacket {

    /** The message to transmit */
    private DataPacket _payload;

    /** The send count of this message (used for packet validation on the other side) */
    private int _sendCount = 0;

    /** The checksum of the entire packet */
    private int _checksum = 0;

    /** The number of bytes in the payload */
    public int length = 0;

    /**
     * Encompass a data packet for communications
     * @param payload
     * @param sendCount
     */
    public ComPacket(DataPacket payload, int sendCount) {
        _payload = payload;
        _sendCount = sendCount;
        length = _payload.length;
        _checksum = generateCheckSum();
    }

    /**
     * Generate a data packet from raw com data
     * @param message
     */
    public ComPacket(byte[] message)
    {
        if(message.length <= 3)
        {
            System.out.println("1");
            throw new IllegalArgumentException("Stream is to short to contain a full packet");
        }

        length = (message[0]&0xFF);
        _sendCount = (message[1]&0xFF);

        _payload = new DataPacket(Arrays.copyOfRange(message, 2, message.length-1));

        _checksum = (message[message.length-1]&0xFF);

        if(length != _payload.length)
        {
            System.out.println("2");
            throw new IllegalArgumentException("Length mismatch");
        }

        if(_checksum != generateCheckSum())
        {
            System.out.println("3");
            throw new IllegalArgumentException("Checksum mismatch");
        }
    }

    /**
     *
     * @return
     */
    public DataPacket getPayload()
    {
        return _payload;
    }

    /**
     *
     * @return
     */
    public int getSendCount()
    {
        return _sendCount;
    }

    /**
     * Convert the packet into a set of bytes for transmission
     * @return
     */
    public byte[] getPacket()
    {
        byte[] toSend = new byte[length+4];

        // add the packet
        for(int i=0;i<_payload.length;i++)
        {
            toSend[i+2] = _payload.getPacket()[i];
        }

        // add header
        toSend[0] = (byte)length;
        toSend[1] = (byte)_sendCount;

        // add footer
        toSend[length+3] = '\n';
        toSend[length+2] = (byte)_checksum;

        return toSend;
    }

    /**
     * Calculate the checksum
     * @return
     */
    private int generateCheckSum()
    {
        int toAdd;
        int check = 0;

        toAdd = (length&0xFF);
        check += ((toAdd * (1)) & 0xFF);

        toAdd = ((_sendCount&0xFF));
        check += ((toAdd * (2)) & 0xFF);

        byte[] packetBytes = _payload.getPacket();

        for(int i=0;i<packetBytes.length;i++)
        {
            toAdd = (packetBytes[i]&0xFF);
            check += ((toAdd * (i+3)) & 0xFF);
        }

        return check & 0xff;
    }
}