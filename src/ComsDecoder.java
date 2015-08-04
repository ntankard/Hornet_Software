import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Nicholas on 4/08/2015.
 */
public class ComsDecoder {

    /** The object that manages all incoming messages */
    private VirtualHornet _virtualHornet;

    /** The queue of messages to consume */
    private BlockingQueue _toConsume = new ArrayBlockingQueue(100);

    /** The worker thread that consumes new messages */
    private Consumer _consumer;

    /**
     *
     * @param theVirtualHornet
     */
    public ComsDecoder(VirtualHornet theVirtualHornet)
    {
        _virtualHornet = theVirtualHornet;
        _consumer = new Consumer(_toConsume,_virtualHornet);
        _consumer.start();
    }

    /**
     * Called my coms when new messages are received
     * Added them to a Queue to be consumed by an external worker thread
     * @param message The message that was recieved
     */
    public void processMessage(char[] message)
    {
        _toConsume.add(message);
    }
}

/**
 * Worker thread to process received messages
 */
class Consumer extends Thread {

    /** The queue of messages to consume */
    private final BlockingQueue _toConsume;

    /** The object that manages all incoming messages */
    private final VirtualHornet _virtualHornet;

    /**
     *
     * @param toConsume
     * @param theVirtualHornet
     */
    Consumer(BlockingQueue toConsume,VirtualHornet theVirtualHornet)
    {
        _toConsume = toConsume;
        _virtualHornet = theVirtualHornet;
    }

    /**
     * Pulls messages from the Queue
     */
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

    /**
     * Decodes received messages and sends them to the hornet manager
     * @param message The message to decode
     */
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