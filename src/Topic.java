import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Topic {
    private int id;
    private List<String> messages;
    private List<Subscriber> subscribers;

    public Topic(int id) {
        this.id = id;
        this.messages = new CopyOnWriteArrayList<>();
        this.subscribers = new CopyOnWriteArrayList<>();
    }

    public int getId() {
        return id;
    }

    public List<String> getMessages() {
        return messages;
    }

    public List<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void addSubscriber(Subscriber subscriber){
        subscribers.add(subscriber);
    }

    public void publishMessage(String message){
        messages.add(message);
        for (Subscriber subscriber : subscribers) subscriber.wakeUp();
    }

    public void resetOffset(Subscriber subscriber, int newOffset){
        subscriber.resetOffset(newOffset);
    }
}
