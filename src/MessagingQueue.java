import java.util.List;

public class MessagingQueue {
    private List<Topic> topics;

    public MessagingQueue(List<Topic> topics) {
        this.topics = topics;
    }

    public void addSubscriber(Topic topic, Subscriber subscriber){
        topic.addSubscriber(subscriber);
        new Thread(subscriber).start();
    }

    public void publishMessage(Topic topic, String message){
        topic.publishMessage(message);
    }

    public void resetOffset(Subscriber subscriber, int newOffset){
        subscriber.getTopic().resetOffset(subscriber, newOffset);
    }
}
