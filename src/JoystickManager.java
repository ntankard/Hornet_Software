//package jinputjoysticktestv2;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Version;
import net.java.games.input.Component.Identifier;
import java.util.ArrayList;

/**
 * 
 * @author endolf 
 * from: http://www.java-gaming.org/index.php/topic,16866.0
 * 
 */

public class JoystickManager {

    private ArrayList<Controller> foundControllers;

    public JoystickManager()
    {
        updateControllerList();
    }

    public void updateControllerList()
    {
        foundControllers = new ArrayList<>();
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

        for(int i = 0; i < controllers.length; i++){
            Controller controller = controllers[i];

            if (controller.getType() == Controller.Type.STICK ||
                controller.getType() == Controller.Type.GAMEPAD ||
                controller.getType() == Controller.Type.WHEEL ||
                controller.getType() == Controller.Type.FINGERSTICK)
            {
                // Add new controller to the list of all controllers.
                foundControllers.add(controller);
            }
        }
    }

    public ArrayList<String> getControllersNames()
    {
        ArrayList<String> toReturn = new ArrayList<>();

        if(!foundControllers.isEmpty()) {
            for(int i = 0; i < foundControllers.size(); i++) {
                toReturn.add(foundControllers.get(i).getName() + " - " + foundControllers.get(i).getType().toString() + " type");
            }
        }
        else {
            toReturn.add("No controller found!");
        }

        return toReturn;
    }

    public JoystickInstance getIntance(Controller controller)
    {
        JoystickInstance toReturn = new JoystickInstance();

        // X axis and Y axis
        int xAxisPercentage = 0;
        int yAxisPercentage = 0;

        // Go trough all components of the controller.
        Component[] components = controller.getComponents();
        for(int i=0; i < components.length; i++)
        {
            Component component = components[i];
            Identifier componentIdentifier = component.getIdentifier();

            // Buttons
            //if(component.getName().contains("Button")){ // If the language is not english, this won't work.
            if(componentIdentifier.getName().matches("^[0-9]*$")){ // If the component identifier name contains only numbers, then this is a button.
                // Is button pressed?
                boolean isItPressed = true;
                if(component.getPollData() == 0.0f)
                    isItPressed = false;

                // Button index
                String buttonIndex;
                buttonIndex = component.getIdentifier().toString(); //@TODO convert to int

                toReturn.buttons.put(buttonIndex, isItPressed);

                // We know that this component was button so we can skip to next component.
                continue;
            }

            // Hat switch
            if(componentIdentifier == Component.Identifier.Axis.POV){
                float hatSwitchPosition = component.getPollData();
                toReturn.hatSwitchPosition=hatSwitchPosition;

                // We know that this component was hat switch so we can skip to next component.
                continue;
            }

            // Axes
            if(component.isAnalog()){
                float axisValue = component.getPollData();
                int axisValueInPercentage = getAxisValueInPercentage(axisValue);

                // X axis
                if(componentIdentifier == Component.Identifier.Axis.X){
                    xAxisPercentage = axisValueInPercentage;
                    toReturn.xAxisPercentage = xAxisPercentage;
                    continue; // Go to next component.
                }
                // Y axis
                if(componentIdentifier == Component.Identifier.Axis.Y){
                    yAxisPercentage = axisValueInPercentage;
                    toReturn.yAxisPercentage = yAxisPercentage;
                    continue; // Go to next component.
                }

                // Other axis
                toReturn.otherAxis.put(component.getName(),axisValueInPercentage);


            }
        }

        return toReturn;
    }

    /**
     * Given value of axis in percentage.
     * Percentages increases from left/top to right/bottom.
     * If idle (in center) returns 50, if joystick axis is pushed to the left/top
     * edge returns 0 and if it's pushed to the right/bottom returns 100.
     *
     * @return value of axis in percentage.
     */
    public int getAxisValueInPercentage(float axisValue)
    {
        return (int)(((2 - (1 - axisValue)) * 100) / 2);
    }



    
}













// TO DELETE
/*

public void getAllControllersInfo()
{
    System.out.println("JInput version: " + Version.getVersion());
    System.out.println("");

    // Get a list of the controllers JInput knows about and can interact with.
    Controller[] controllersList = ControllerEnvironment.getDefaultEnvironment().getControllers();

    // First print all controllers names.
    for(int i =0;i<controllersList.length;i++){
        System.out.println(controllersList[i].getName());
    }

    // Print all components of controllers.
    for(int i = 0; i < controllersList.length; i++){
        System.out.println("\n");
        System.out.println("-----------------------------------------------------------------");

        // Get the name of the controller
        System.out.println(controllersList[i].getName());
        // Get the type of the controller, e.g. GAMEPAD, MOUSE, KEYBOARD,
        // see http://www.newdawnsoftware.com/resources/jinput/apidocs/net/java/games/input/Controller.Type.html
        System.out.println("Type: "+controllersList[i].getType().toString());

        // Get this controllers components (buttons and axis)
        Component[] components = controllersList[i].getComponents();
        System.out.print("Component count: "+components.length);
        for(int j=0; j<components.length; j++){
            System.out.println("");

            // Get the components name
            System.out.println("Component "+j+": "+components[j].getName());
            // Get it's identifier, E.g. BUTTON.PINKIE, AXIS.POV and KEY.Z,
            // see http://www.newdawnsoftware.com/resources/jinput/apidocs/net/java/games/input/Component.Identifier.html
            System.out.println("    Identifier: "+ components[j].getIdentifier().getName());
            System.out.print("    ComponentType: ");
            if (components[j].isRelative())
                System.out.print("Relative");
            else
                System.out.print("Absolute");

            if (components[j].isAnalog())
                System.out.print(" Analog");
            else
                System.out.print(" Digital");
        }

        System.out.println("\n");
        System.out.println("-----------------------------------------------------------------");
    }
}


public void pollControllerAndItsComponents(Controller.Type controllerType)
    {
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

        // First controller of the desired type.
        Controller firstController = null;

        for(int i=0; i < controllers.length && firstController == null; i++) {
            if(controllers[i].getType() == controllerType) {
                // Found a controller
                firstController = controllers[i];
                break;
            }
        }

        if(firstController == null) {
            // Couldn't find a controller
            System.out.println("Found no desired controller!");
            System.exit(0);
        }

        System.out.println("First controller of a desired type is: " + firstController.getName());

        while(true) {
            firstController.poll();
            Component[] components = firstController.getComponents();
            StringBuffer buffer = new StringBuffer();
            for(int i=0;i<components.length;i++) {
                if(i>0) {
                buffer.append(", ");
                }
                buffer.append(components[i].getName());
                buffer.append(": ");
                if(components[i].isAnalog()) {
                    buffer.append(components[i].getPollData());
                } else {
                    if(components[i].getPollData()==1.0f) {
                        buffer.append("On");
                    } else {
                        buffer.append("Off");
                    }
                }
            }
            System.out.println(buffer.toString());

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

 */
