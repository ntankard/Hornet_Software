import com.digi.xbee.api.exceptions.XBeeException;

/**
 * Created by Nicholas on 2/08/2015.
 */
public class VirtualHornet {

    /** The system that manages low level communications */
    private Coms _coms;

    /** The system taht encodes messages into spendable packets */
    private ComsEncoder _comsEncoder;

    /** The UI */
    private Navigation _navigation;

    /** The state of the entire system */
    enum State{Init,Idle,Connect,Connected}
    private State _state;

    /**
     * Default constructor
     * Dose nothing because components are not added yet
     */
    public VirtualHornet()
    {
        _state =State.Init;
    }

    /**
     * Attach all components that need to be constructed externaly
     */
    public void attachNavigation(Navigation theNav){ _navigation = theNav; }
    public void attachComs(Coms theComs){_coms = theComs;}
    public void attachComsEncoder(ComsEncoder theComsEncoder){_comsEncoder = theComsEncoder;}

    /**
     * Start the system (equivalent to a constructor, call after all componets are attached)
     */
    public void start()
    {
        _navigation.setComPorts(Coms.getPorts());
        _navigation.open();

        _state =State.Idle;
    }

    /**
     * The user is attempting to connect to the drone
     * @param comPort The com port to use to connect to the drone
     */
    public void UI_connect(String comPort,int baudRate ){
        if(_state == State.Idle) {
            _navigation.setConnectionState(Navigation.ConnectionState.Connecting);
            _state =  State.Connect;
            if(!_coms.open(comPort,baudRate))
            {
                _navigation.setConnectionState(Navigation.ConnectionState.Disconnected);
                //@TODO add failure notification
            }
         }else
         {
             //@toDO throw
         }
    }

    /**
     * A message has been received from the drone requesting to open a connection
     */
    public void C_connectRequest()
    {
        if(_state == State.Connect)
        {
            _state = State.Connected;
            _navigation.setConnectionState(Navigation.ConnectionState.Connected);
        }
    }




}
