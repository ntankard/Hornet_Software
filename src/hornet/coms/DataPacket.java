package hornet.coms;

import java.util.Arrays;

/**
 * Created by Nicholas on 8/10/2015.
 */
public class DataPacket {

    private int _ID;

    private byte[] _packet;

    public DataPacket(int _ID, byte[] _packet) {
        this._packet = _packet;
        this._ID = _ID;
    }

    public DataPacket(int _ID) {
        this._ID = _ID;
    }

    public DataPacket(byte[] stream) {
        _ID = stream[0];
        if(stream.length != 1)
        {
            _packet = Arrays.copyOfRange(stream,1,stream.length );
        }

    }

    public byte[] getBytes()
    {
        byte[] toReturn = new byte[length()];

        toReturn[0] = (byte)_ID;

        if(_packet != null)
        {
            for(int i=0;i<_packet.length;i++)
            {
                toReturn[i+1] = _packet[i];
            }
        }

        return toReturn;
    }


    public byte[] get_packet() {
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

    public int length()
    {
        if(_packet == null)
        {
            return 1;
        }
        return _packet.length + 1;
    }

}
