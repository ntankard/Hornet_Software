package hornet.coms;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

/**
 * Created by Nicholas on 8/10/2015.
 */
public class DataPacket {

    /** The ID of the message */
    private int _ID;

    /** The raw payload */
    private byte[] _bytePayload;

    /** A SHort version of the raw payload */
    private short[] _shortPayload;

    /** The number of bytes in the payload */
    public int length;

    /**
     * Construct a packet with no payload
     * @param ID    The ID to set
     */
    public DataPacket(int ID) {
        _ID = ID;
        length = 1;
    }

    /**
     * Construct a packet using the raw payload
     * @param ID        The ID of the packet
     * @param payload   The payload
     */
    public DataPacket(int ID, byte[] payload) {
        _bytePayload = payload;
        _ID = ID;
        _shortPayload = toShortArray(payload);
        length = 1+ payload.length;
    }

    /**
     * Construct a packet from a short array
     * @param ID        The ID of the packet
     * @param payload   The payload
     */
    public DataPacket(int ID, short[] payload)
    {
        _shortPayload = payload;
        _ID = ID;
        _bytePayload = toByteArray(payload);
        length = 1+ payload.length;
    }

    /**
     * Construct a packet from a stream of bytes
     * @param stream    The stream of bytes
     */
    public DataPacket(byte[] stream) {
        _ID = stream[0];
        if(stream.length != 1)
        {
            _bytePayload = Arrays.copyOfRange(stream,1,stream.length );
            _shortPayload = toShortArray(_bytePayload);
        }
        length = stream.length;
    }

    /**
     * Convert a array of bytes to a array of short
     * @param message   The array of bytes
     * @return  THe array of short
     */
    private short[] toShortArray(byte[] message)
    {
        int floats = message.length/2;
        byte[] buffer = new byte[2];
        byte[] toConvert = new byte[message.length];
        for(int i=0;i<floats;i++)
        {
            buffer[0] = message[0+(2*i)];
            buffer[1] = message[1+(2*i)];

            message[0+(2*i)] = buffer[0];
            message[1+(2*i)] = buffer[1];
            toConvert[0+(2*i)] = buffer[1];
            toConvert[1+(2*i)] = buffer[0];

        }

        final ShortBuffer fb = ByteBuffer.wrap(toConvert).asShortBuffer();
        final short[] dst = new short[fb.capacity()];
        fb.get(dst); // Copy the contents of the FloatBuffer into dst
        return dst;
    }

    /**
     * Convert a array of short to a array of byte
     * @param message   The array of short
     * @return  The array of bytes
     */
    private byte[] toByteArray(short[] message)
    {
        byte[] toReturn = new byte[message.length*2];
        ByteBuffer bb = ByteBuffer.allocate(message.length * 2);
        bb.asShortBuffer().put(message);
        byte[] b= bb.array();

        byte[] buffer = new byte[2];
        for(int i=0;i<message.length;i++)
        {
            toReturn[(i*2)] = b[(i*2)+1];
            toReturn[(i*2)+1] = b[(i*2)];
        }

        return toReturn;
    }

    /**
     * Converts the DataPacket to an array of byte to be sent
     * @return  The array of bytes (ID and payload)
     */
    public byte[] getPacket()
    {
        byte[] toReturn = new byte[length];

        toReturn[0] = (byte)_ID;

        if(_bytePayload != null)
        {
            for(int i=0;i<_bytePayload.length;i++)
            {
                toReturn[i+1] = _bytePayload[i];
            }
        }

        return toReturn;
    }

    /**
     * Get the ID of the packet
     * @return  The ID
     */
    public int getID()
    {
        return _ID;
    }

    public short[] getShortPayload()
    {
        return _shortPayload;
    }
}