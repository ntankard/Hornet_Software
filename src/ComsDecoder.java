import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Nicholas on 4/08/2015.
 */
public class ComsDecoder {

    private VirtualHornet _virtualHornet;
    private BlockingQueue _toConsume = new ArrayBlockingQueue(100);
    private Consumer _consumer;

    public ComsDecoder(VirtualHornet theVirtualHornet)
    {
        _virtualHornet = theVirtualHornet;
        _consumer = new Consumer(_toConsume,_virtualHornet);
    }

    public void processMessage(char[] message)
    {
        _toConsume.add(message);
    }
}


class Consumer extends Thread {

    private final BlockingQueue _toConsume;
    private final VirtualHornet _virtualHornet;

    Consumer(BlockingQueue toConsume,VirtualHornet theVirtualHornet)
    {
        _toConsume = toConsume;
        _virtualHornet = theVirtualHornet;
    }

    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            try {
                processMessage((char[]) _toConsume.take());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
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