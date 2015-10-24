package hornet;

import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * Created by Nicholas on 19/10/2015.
 */
public class BinaryAverage {

    int _window;

    int _onCount;

    FixedCircularBuffer<Boolean> _buffer;

    public BinaryAverage(int window)
    {
        _window = window;
        _buffer = new FixedCircularBuffer<>(window);
    }

    public void newData(boolean isOn)
    {
        Boolean removed = _buffer.add(isOn);

        if(removed != null)
        {
            if(removed)
            {
                _onCount--;
            }
        }

        if(isOn)
        {
            _onCount++;
        }
    }

    public double getOnPercentage()
    {
        return 100.0*((double)_onCount / (double)_buffer.getFill());
    }

}
