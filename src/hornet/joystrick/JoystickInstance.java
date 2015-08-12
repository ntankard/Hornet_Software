package hornet.joystrick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Nicholas on 20/07/2015.
 */
public class JoystickInstance {



    private int _xAxisPercentage;
    private int _yAxisPercentage;
    private HashMap<String,Boolean> _buttons = new HashMap<>(); //@TODO convert to int
    private float _hatSwitchPosition;
    private HashMap<String,Integer> _otherAxis = new HashMap<>();

    public JoystickInstance(int xAxisPercentage,int yAxisPercentage,HashMap<String,Boolean> buttons,float hatSwitchPosition,HashMap<String,Integer> otherAxis)
    {
        _xAxisPercentage = xAxisPercentage;
        _yAxisPercentage=yAxisPercentage;
        _buttons = buttons;
        _hatSwitchPosition = hatSwitchPosition;
        _otherAxis = otherAxis;
    }

    public int getX() {
        return _xAxisPercentage;
    }

    public int getY() {
        return _yAxisPercentage;
    }

    public HashMap<String, Boolean> getButtons() {
        return _buttons;
    }

    public float getHatSwitchPosition() {
        return _hatSwitchPosition;
    }

    public HashMap<String, Integer> getOtherAxis() {
        return _otherAxis;
    }

    public void setX(int _xAxisPercentage) {
        this._xAxisPercentage = _xAxisPercentage;
    }

    public void setY(int _yAxisPercentage) {
        this._yAxisPercentage = _yAxisPercentage;
    }

    public void setHatSwitchPosition(float _hatSwitchPosition) {
        this._hatSwitchPosition = _hatSwitchPosition;
    }

    public void setButton(String key,boolean isPressed)
    {
        if(_buttons.put(key,isPressed) == null)
        {
            //@TODO add error here (not in previously)
        }
    }

    public void setOtherAxis(String key,int value)
    {
        if(_otherAxis.put(key,value) == null)
        {
            //@TODO add error here (not in previously)
        }
    }

    /**
     * Check to see if the X or Y values of the joystick have changed
     * @param j The instance to compare to
     * @return Have the XY values changed
     */
    public boolean isEqualXY(JoystickInstance j)
    {
        if(j._xAxisPercentage != _xAxisPercentage || j._yAxisPercentage != _yAxisPercentage)
        {
            return false;
        }
        return true;
    }

    /**
     * Check to see if a specific other axis have changed between instances
     * @param j The instance to compare to
     * @param key The other axis to check
     * @return Has the axis changed
     */
    public boolean isEqualOtherAxis(JoystickInstance j, String key)
    {
        if(_otherAxis.get(key) != j._otherAxis.get(key))
        {
            return false;
        }
        return true;
    }

}
