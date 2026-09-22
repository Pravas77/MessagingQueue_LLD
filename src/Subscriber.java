import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Subscriber implements Runnable {
    private int id;
    private Topic topic;
    private AtomicInteger offset;
    private Object moniter = new Object();

    public Subscriber(int id, Topic topic) {
        this.id = id;
        this.topic = topic;
        this.offset = new AtomicInteger(0);
    }

    public void run() {

        List<String> messages = topic.getMessages();
        while (true) {


            synchronized (moniter) {
                while (offset.get() >= messages.size()) {
                    try {
                        moniter.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }


            int currentIndex = offset.get();
            consume(messages.get(currentIndex));
            offset.compareAndSet(currentIndex, currentIndex + 1);

        }

    }


    public void wakeUp() {
        synchronized (moniter) {
            moniter.notify();
        }
    }


    public void consume(String message) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("subscriber id " + id + " consumed message ->  " + message);
    }


    public void resetOffset(int newOffset) {
        offset.set(newOffset);
        wakeUp();
    }

    public int getId() {
        return id;
    }

    public Topic getTopic() {
        return topic;
    }

    public AtomicInteger getOffset() {
        return offset;
    }

}
