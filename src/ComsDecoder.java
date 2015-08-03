/**
 * Created by Nicholas on 4/08/2015.
 */
public class ComsDecoder {

    private VirtualHornet _virtualHornet;

    public ComsDecoder(VirtualHornet theVirtualHornet)
    {
        _virtualHornet = theVirtualHornet;
    }

    public void processMessage(char[] message)
    {
        switch (message[0])
        {
            case CONFIG.Coms.PacketCodes.CONNECTION_REQUEST:
                _virtualHornet.C_connectRequest();
                break;
            default:
                break;
        }

    }
}
