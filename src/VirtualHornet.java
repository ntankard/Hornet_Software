/**
 * Created by Nicholas on 2/08/2015.
 */
public class VirtualHornet {

    enum State{Init,Idle,Connect};

    private State state;

    public VirtualHornet()
    {
        state=State.Init;
    }

    public void attachNavigation(Navigation theNav)
    {
        _navigation = theNav;
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

    public void connect(String comPortA )
    {
        if(state == State.Idle) {
            _navigation.setConnectionState(Navigation.ConnectionState.Connecting);
         }else
         {
             //@toDO throw
         }
    }


    private Navigation _navigation;

}
