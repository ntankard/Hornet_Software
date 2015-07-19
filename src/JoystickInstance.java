import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nicholas on 20/07/2015.
 */
public class JoystickInstance {

    public int xAxisPercentage;
    public int yAxisPercentage;

    public HashMap<String,Boolean> buttons = new HashMap<>(); //@TODO convert to int
    float hatSwitchPosition;

    public HashMap<String,Integer> otherAxis = new HashMap<>();
}
