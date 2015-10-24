package hornet;

import java.util.Objects;

/**
 * Created by Nicholas on 19/10/2015.
 */
public class FixedCircularBuffer<T> {

    private int _index = 0;

    private Object[] _buffer;

    private int _size;

    private int _added =0;

    public FixedCircularBuffer(int Size)
    {
        _buffer = new Object[Size];
        _size = Size;
    }

    public int getFill()
    {
        return _added;
    }

    public T add(T toAdd)
    {
        if(_added < _size)
        {
            // initial fill up
            _buffer[_index] = toAdd;
            _index++;
            if(_index >= _size)
            {
                _index =0;
            }
            _added++;

            return null;
        }
        else
        {
            // regular operation
            Object toReturn = _buffer[_index];
            _buffer[_index] = toAdd;
            _index++;
            if(_index >= _size)
            {
                _index =0;
            }
            return (T)toReturn;
        }
    }

}
