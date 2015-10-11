package hornet.coms;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

/**
 * Created by Nicholas on 8/10/2015.
 */
public class DataPacket {

    private int _ID;

    private byte[] _bytePayload;

    private short[] _shortPayload;

    public int length;

    public DataPacket(int ID, byte[] payload) {
        _bytePayload = payload;
        _ID = ID;
        _shortPayload = toShortArray(payload);
        length = 1+ payload.length;
    }

    public DataPacket(int ID, short[] payload)
    {
        _shortPayload = payload;
        _ID = ID;
        _bytePayload = toByteArray(payload);
        length = 1+ payload.length;
    }

    public DataPacket(int ID) {
        _ID = ID;
        length = 1;
    }

    public DataPacket(byte[] stream) {
        _ID = stream[0];
        if(stream.length != 1)
        {
            _bytePayload = Arrays.copyOfRange(stream,1,stream.length );
            _shortPayload = toShortArray(_bytePayload);
        }
        length = stream.length;
    }

    private short[] toShortArray(byte[] message)
    {
        int floats = message.length/2;
        byte[] buffer = new byte[2];
        for(int i=0;i<floats;i++)
        {
            buffer[0] = message[0+(2*i)];
            buffer[1] = message[1+(2*i)];

            message[0+(2*i)] = buffer[1];
            message[1+(2*i)] = buffer[0];
        }

        final ShortBuffer fb = ByteBuffer.wrap(message).asShortBuffer();
        final short[] dst = new short[fb.capacity()];
        fb.get(dst); // Copy the contents of the FloatBuffer into dst
        return dst;
    }

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

    public int getID()
    {
        return _ID;
    }

  /*  public byte[] get_packet() {
        return _packet;
    }

    public void set_packet(byte[] _packet) {
        this._packet = _packet;
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }
*/
}
