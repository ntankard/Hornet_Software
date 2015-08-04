import java.io.IOException;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Nicholas on 4/08/2015.
 */
public class ComsEncoder {

    private Coms _coms;
    //private Queue<byte[]>[] _messages;
    private Sender _sender;
    private PriorityBlockingQueue<Message> _messages;

    public ComsEncoder(Coms theComs)
    {
        Comparator<Message> comparator = new MessageComparator();
        _messages = new PriorityBlockingQueue<>(10,comparator);

        _coms = theComs;
        _sender = new Sender(_messages,_coms);
        _sender.start();
    }

    public void send_connectionConfirmation()
    {
        byte[] theMessage = new byte[1];
        theMessage[0] = 'b';

        _messages.add(new Message(theMessage,0));
    }
}

class Sender extends Thread {

    private PriorityBlockingQueue<Message> _messages;
    private Coms _coms;

    Sender(PriorityBlockingQueue<Message> toSend,Coms theComs)
    {
        _messages = toSend;
        _coms = theComs;
    }

    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            try {
                send(_messages.take().message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public void send(byte[] theMessage) {

        // wait for the chanel to be clear
        while(!_coms.canSend())
        {
            try {
                wait(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //send the message
        try {
            _coms.send(theMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Message
{
    public Message(byte[] theMessage,int thePriority)
    {
        priority=thePriority;
        message = theMessage;
    }
    int priority;
    byte[] message;
}

class MessageComparator implements Comparator<Message>
{
    @Override
    public int compare(Message x, Message y)
    {
        if (x.priority < y.priority)
        {
            return -1;
        }
        if (x.priority > y.priority)
        {
            return 1;
        }
        return 0;
    }
}
