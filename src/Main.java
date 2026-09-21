import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("Hello");

        Topic topic1 = new Topic(1);
        Topic topic2 = new Topic(2);

        Subscriber subscriber1 = new Subscriber(1,topic1);
        Subscriber subscriber2 = new Subscriber(2,topic1);
        Subscriber subscriber3 = new Subscriber(3,topic2);

        MessagingQueue messagingQueue = new MessagingQueue(List.of(topic1,topic2));
        messagingQueue.addSubscriber(topic1,subscriber1);
        messagingQueue.addSubscriber(topic1,subscriber2);
        messagingQueue.addSubscriber(topic2,subscriber3);


        Thread.sleep(500);
        // topic1
        messagingQueue.publishMessage(topic1,"1st message from topic1");
        messagingQueue.publishMessage(topic1,"2nd message from topic1");
        messagingQueue.publishMessage(topic1,"3rd message from topic1");
        // tpoic2
        messagingQueue.publishMessage(topic2,"1st message from topic2");
        messagingQueue.publishMessage(topic2,"2nd message from topic2");


       Thread.sleep(5000);
       System.out.println("cheking with time delay");
       messagingQueue.publishMessage(topic1,"4th message from topic1");


        Thread.sleep(5000);
        System.out.println("checking the resetOffset api");
        messagingQueue.resetOffset(subscriber1,1);


    }
}