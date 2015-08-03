import com.digi.xbee.api.exceptions.XBeeException;

/**
 * Created by Nicholas on 2/08/2015.
 */
public class VirtualHornet {

    enum State{Init,Idle,Connect}

    private State state;

    public VirtualHornet()
    {
        state=State.Init;
    }

    public void attachNavigation(Navigation theNav)
    {
        _navigation = theNav;
    }

    private Coms _coms;

    public void attachComs(Coms theComs)
    {
        _coms = theComs;
    }

    public void start()
    {
        _navigation.setComPorts(Coms.getPorts());
        _navigation.open();

        state=State.Idle;
    }

    public void run()
    {

    }

    public void connect(String comPortA ) throws XBeeException {
        if(state == State.Idle) {
            _navigation.setConnectionState(Navigation.ConnectionState.Connecting);
            state =  State.Connect;
            _coms.open(comPortA,9600);
         }else
         {
             //@toDO throw
         }
    }


    private Navigation _navigation;

}
