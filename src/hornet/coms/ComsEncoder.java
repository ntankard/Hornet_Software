package hornet.coms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import hornet.gui.Navigation;

/**
 * Created by Nicholas on 4/08/2015.
 */
public class ComsEncoder {

    private Coms _coms;
    private Sender _sender;
    private PriorityBlockingQueue<Message> _messages;
    private Navigation _navigation;

    public ComsEncoder(Coms theComs,Navigation theNavigation)
    {
        Comparator<Message> comparator = new MessageComparator();
        _messages = new PriorityBlockingQueue<>(10,comparator);

        _coms = theComs;
        _navigation = theNavigation;
        _sender = new Sender(_messages,_coms);
        _sender.start();
    }

    public void send_connectionConfirmation()
    {
        byte[] theMessage = new byte[1];
        theMessage[0] = 'b';

        send(theMessage, 0);
    }

    public void send(byte[] message,int priority)
    {
        _messages.add(new Message(message, priority));
        try {
            _navigation.newSentMessage(new String(message, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Worker thread to send messages
 */
class Sender extends Thread {

    /** The messages to send */
    private PriorityBlockingQueue<Message> _messages;

    /** The com object to send through */
    private Coms _coms;

    /**
     *
     * @param toSend The Queue of messages to send
     * @param theComs The com object to send them through
     */
    Sender(PriorityBlockingQueue<Message> toSend,Coms theComs)
    {
        _messages = toSend;
        _coms = theComs;
    }

    /**
     * Pulls messages from the queue
     */
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

    /**
     * Send a single message
     * @param theMessage The message to send
     */
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

/**
 * A message and its priority used as the object for a PriorityBlockingQueue
 */
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

/**
 * Compares the priories of 2 Messages
 */
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
