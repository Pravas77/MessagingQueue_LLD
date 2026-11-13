# Messaging Queue (Pub-Sub) – Low Level Design

An in-memory, Kafka-style publish-subscribe messaging queue in Java. Publishers write messages to topics, and every subscriber reads a topic independently on its own thread, tracking its own offset.

## Features
- Multiple topics
- Multiple subscribers per topic; each one receives every message
- Each subscriber consumes on its own thread at its own pace
- Per-subscriber **offset**, like a Kafka consumer offset
- **Reset offset** to replay messages from any position
- Subscribers sleep while there is nothing to read and wake up when a message arrives (no busy waiting)

## Design

| Class | Responsibility |
|---|---|
| `MessagingQueue` | Entry point: add subscribers (and start their threads), publish messages, reset offsets |
| `Topic` | Append-only message log plus its list of subscribers; wakes subscribers on publish |
| `Subscriber` | `Runnable` that keeps its own `AtomicInteger` offset and consumes messages in order |

```
Publisher ──publish──▶ Topic [m0, m1, m2, m3, ...]   (append-only log)
                           │
              ┌────────────┼────────────┐
         Subscriber 1  Subscriber 2  Subscriber 3
          offset = 3     offset = 1    offset = 0
```

## Concurrency
- **Message log:** messages and subscribers are stored in `CopyOnWriteArrayList`s, so subscribers can read safely while publishers append.
- **Waiting for messages:** each subscriber waits on its own monitor (`wait()` inside a `while` loop to handle spurious wake-ups). `publishMessage` calls `wakeUp()` (`notify()`) on every subscriber of the topic.
- **Offset reset during consumption:** after consuming a message, the offset is advanced with `compareAndSet(current, current + 1)`. If the offset was reset while a message was being processed, the CAS fails and the reset value is kept instead of being overwritten.

## Design patterns
- **Observer / Publish-Subscribe** – topics notify their subscribers when new messages arrive.

## How to run
Requires Java 17+.
```bash
javac -d out src/*.java
java -cp out Main
```
`Main` creates two topics with three subscribers (two on topic 1, one on topic 2), publishes messages, publishes again after a delay to show subscribers waking up, and finally resets subscriber 1's offset to 1 to replay messages. Subscriber threads keep waiting for new messages, so stop the program with `Ctrl + C`.

## Possible extensions
- Consumer groups, where each message goes to only one consumer in the group
- Partitions per topic for parallel consumption
- Message retention and graceful shutdown of subscriber threads
