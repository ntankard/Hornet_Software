package hornet;

/**
 * Created by Nicholas on 17/09/2015.
 */
public class PacketInfo {

    private int _size;
    private boolean _toHornet;

    public PacketInfo(int _size,  boolean _toHornet,int _comPriority) {
        this._size = _size;
        this._comPriority = _comPriority;
        this._toHornet = _toHornet;
    }

    private int _comPriority;

    public int get_comPriority() {
        return _comPriority;
    }

    public void set_comPriority(int _comPriority) {
        this._comPriority = _comPriority;
    }

    public int get_size() {
        return _size;
    }

    public void set_size(int _size) {
        this._size = _size;
    }

    public boolean is_toHornet() {
        return _toHornet;
    }

    public void set_toHornet(boolean _toHornet) {
        this._toHornet = _toHornet;
    }
}
