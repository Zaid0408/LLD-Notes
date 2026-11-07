// Problem statement: 

/*

Design and implement a Notification Engine in Java that allows an application to send notifications to users through multiple communication channels.
The system should be flexible enough to support different notification types, additional notification features, and real-time notification logging.

The application should be designed so that adding new notification types or functionality does not require significant changes to existing code.
Requirements
1. Multiple Notification Types
The system should support three notification channels:
- Email
- SMS
- Push Notification
Each channel should have its own implementation for sending notifications.
2. Notification Details
Each notification should contain:
- Notification ID
- Recipient details
- Message
- Notification type
- Timestamp
- Current status
Possible statuses:
PENDING
SENT
FAILED


3. Notification Enhancements
The system should support adding additional functionality to notifications.

For example:
- Retry mechanism if notification delivery fails.
- High-priority notification handling.
- Additional message formatting.
These enhancements should be optional and combinable.
4. Real-Time Notification Logging
Every notification activity should be logged.

For example:
Notification Created
Notification Sending Started
Notification Sent Successfully
Notification Failed


The system should support multiple components interested in notification events, such as:
- Notification Logger
- Analytics Service
- Audit Service
These components should receive updates whenever relevant notification events occur.
5. Notification Management
The system should provide a central service responsible for managing the notification workflow.

It should be able to:
1. Accept a notification request.
2. Identify the notification channel.
3. Apply the requested notification enhancements.
4. Send the notification.
5. Update its status.
6. Trigger the appropriate notification events.


6. Extensibility
The design should allow future additions such as:
- WhatsApp notifications.
- Telegram notifications.
- New notification enhancements.
- Additional notification event listeners.
Existing notification implementations should not require modification whenever a new type is introduced.

Design Constraints
Your implementation must demonstrate the following four design patterns:
Pattern	Required
Strategy	Yes
Observer	Yes
Decorator	Yes
Singleton	Yes

Implementation constraints
- Use Java.
- Only one .java file should be required.
- You can create multiple classes, interfaces and enums within that file.
- No Spring Boot or external libraries.
- No database or actual third-party notification APIs.
- Use console output to simulate sending notifications and logging events.
- Keep the implementation at SDE1 LLD level. No distributed systems, Kafka, RabbitMQ or microservices required.
Expected outcome
Design a complete Notification Engine with clean separation of responsibilities, flexible notification delivery, optional enhancements and real-time event logging.

Your challenge: Design the class structure and decide how to use the four patterns together. Don't force a pattern where it doesn't naturally fit.

Implement the complete solution and share your Java code for review.
*/
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class NoficationNewSystem {

    public static void main(String[] args) {

        // 1. Get Singleton NotificationStore
        NotificationStore store = NotificationStore.getInstance();

        // 2. Create and register recipient
        Recipient recipient = new Recipient(
            1L,
            "Alice",
            "alice@gmail.com",
            "9876543210",
            "alice_device_token"
        );

        store.addRecipient(recipient);

        // 3. Configure Strategies
        Map<DeliveryChannel, DeliveryStrategy> strategies =
                new EnumMap<>(DeliveryChannel.class);

        strategies.put(DeliveryChannel.EMAIL, new EmailNotification());
        strategies.put(DeliveryChannel.SMS, new SMSNotification());
        strategies.put(DeliveryChannel.PUSH, new PushNotification());

        // 4. Create Notification Service
        NotificationPush service = new NotificationPush(strategies);

        // 5. Register Observers
        service.addListener(new NotificationLogger());
        service.addListener(new NotificationAnalytics());
        service.addListener(new NotificationAudit());

        // 6. Create Email Notification
        Notification email = new Notification(
            101L, 1L, "Welcome to our application!",
            DeliveryChannel.EMAIL, true, true
        );

        store.addNotification(email);
        service.pushNotification(email);

        System.out.println("\n-------------------------\n");

        // 7. Create SMS Notification
        Notification sms = new Notification(
            102L, 1L, "Your OTP is 123456",
            DeliveryChannel.SMS, false, true
        );

        store.addNotification(sms);
        service.pushNotification(sms);

        System.out.println("\n-------------------------\n");

        // 8. Create Push Notification
        Notification push = new Notification(
            103L, 1L, "Your order has been shipped!",
            DeliveryChannel.PUSH, true, false
        );

        store.addNotification(push);
        service.pushNotification(push);
    }
}

class NotificationStore {
    /*
    Because it is static, it belongs to the Class, not the Object.
• static variables are initialized only once when the Java Virtual Machine (JVM) loads the NotificationStore class into memory.
• When the class loads, Java executes new NotificationStore() exactly one time to create the object and stores its memory address in the INSTANCE variable.
If you omitted the static keyword, writing NotificationStore instance = new NotificationStore(); inside the class would crash your program with a StackOverflowError.
• Without static, every time you created a NotificationStore, it would try to create another NotificationStore inside itself, which creates another, and another—infinitely.

    */
    private static final NotificationStore INSTANCE = new NotificationStore();

    private final Map<Long, Recipient> recipients = new HashMap<>();
    private final Map<Long, Notification> notifications = new HashMap<>(); // todo: ConcurrentHashMap for thread safety

    private NotificationStore() {
    }

    public static NotificationStore getInstance() {
        return INSTANCE;
    }

    public void addRecipient(Recipient recipient) {
        recipients.put(recipient.id, recipient);
    }

    public Optional<Recipient> findRecipient(long id) {
        return Optional.ofNullable(recipients.get(id));
    }

    public void addNotification(Notification notification) {
        notifications.put(notification.id, notification);
    }

    public Optional<Notification> findNotification(long id) {
        return Optional.ofNullable(notifications.get(id));
    }
}

class Recipient{
    String name,email,phone,pushToken;
    long id;
    Recipient(long id, String name, String email,String phone, String pushToken) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pushToken = pushToken;
    }
}

enum Status{
    CREATED,PENDING,SENT,FAILED;
}
enum DeliveryChannel{
    EMAIL,SMS,PUSH
}
class Notification{
    long id;
    long recipientId;
    Status status;
    DeliveryChannel channel;
    String message;
    Instant createdAt;
    boolean highPriority;
    boolean retryEnabled;
    Notification(long id, long recipientId, String message,DeliveryChannel channel, boolean highPriority, boolean retryEnabled) {

        this.id = id;
        this.recipientId = recipientId;
        this.message = message;
        this.channel = channel;

        this.highPriority = highPriority;
        this.retryEnabled = retryEnabled;

        this.status = Status.CREATED;
        this.createdAt = Instant.now();
    }
}

// Decorator pattern for notification enhancements

// Abstract decorator

abstract class DeliveryDecorator implements DeliveryStrategy {

    protected DeliveryStrategy strategy;

    public DeliveryDecorator(DeliveryStrategy strategy) {
        this.strategy = strategy;
    }
}

class RetryDecorator extends DeliveryDecorator {
    int maxAttempts;
    public RetryDecorator(DeliveryStrategy strategy, int maxAttempts) {
        super(strategy);
        this.maxAttempts = maxAttempts;
    }
    @Override
    public boolean sendNotification(Notification notification) {
        for (int i = 1; i <= maxAttempts; i++) {
            System.out.println("Delivery attempt: " + i);
            boolean sent = strategy.sendNotification(notification);
            if (sent) {
                return true;
            }
            System.out.println("Delivery failed. Retrying...");
        }
        return false;
    }
}

class PriorityDecorator extends DeliveryDecorator {
    public PriorityDecorator(DeliveryStrategy strategy) {
        super(strategy);
    }
    @Override
    public boolean sendNotification(Notification notification) {

        System.out.println(
            "Processing HIGH PRIORITY notification: " + notification.id
        );
        return strategy.sendNotification(notification);
    }
}

// Strategy for email,sms and push

interface DeliveryStrategy {
    boolean sendNotification(Notification notification);
}

class EmailNotification implements DeliveryStrategy{
    @Override 
    public boolean sendNotification(Notification notification)
    {
        long recipientId=notification.recipientId;
        if(recipientId==-1)
        {
            throw new RuntimeException("Recipient not found");
        }
            
        if(notification.message==null || notification.message.isEmpty())
        {
            throw new RuntimeException("Message is empty");
        }
        if(notification.status==Status.SENT || notification.status==Status.FAILED)
        {
            throw new RuntimeException("Notification already sent");
        }
        Recipient recipient=NotificationStore.getInstance()
            .findRecipient(recipientId).orElse(null);
        if(recipient==null)
            throw new RuntimeException("Recipient not found");
        if(recipient.email==null || recipient.email.isEmpty())
            throw new RuntimeException("Recipient email not found");
        System.out.println("Sending Email to "+recipient.email+" with message: "+notification.message);
        return true;
    }
}

class SMSNotification implements DeliveryStrategy{
    @Override 
    public boolean sendNotification(Notification notification)
    {
        long recipientId=notification.recipientId;
        if(recipientId==-1)
        {
            throw new RuntimeException("Recipient not found");
        }
            
        if(notification.message==null || notification.message.isEmpty())
        {
            throw new RuntimeException("Message is empty");
        }
        if(notification.status==Status.SENT || notification.status==Status.FAILED)
        {
            throw new RuntimeException("Notification already sent");
        }
        Recipient recipient=NotificationStore.getInstance()
            .findRecipient(recipientId).orElse(null);
        if(recipient==null)
            throw new RuntimeException("Recipient not found");
        if(recipient.phone==null || recipient.phone.isEmpty())
            throw new RuntimeException("Recipient phone not found");
        System.out.println("Sending SMS to "+recipient.phone+" with message: "+notification.message);
        return true;
    }
}

class PushNotification implements DeliveryStrategy{
    @Override 
    public boolean sendNotification(Notification notification)
    {
        long recipientId=notification.recipientId;
        if(recipientId==-1)
        {
            throw new RuntimeException("Recipient not found");
        }
            
        if(notification.message==null || notification.message.isEmpty())
        {
            throw new RuntimeException("Message is empty");
        }
        if(notification.status==Status.SENT || notification.status==Status.FAILED)
        {
            throw new RuntimeException("Notification already sent");
        }
        Recipient recipient=NotificationStore.getInstance()
            .findRecipient(recipientId).orElse(null);
        if(recipient==null)
            throw new RuntimeException("Recipient not found");
        if(recipient.pushToken==null || recipient.pushToken.isEmpty())
            throw new RuntimeException("Recipient push token not found");
        System.out.println("Sending Push Notification to "+recipient.pushToken+" with message: "+notification.message);
        return true;
    }
}

// Observer pattern for notification events

interface NotificationPusher{
    void pushNotification(Notification notification);
    void notifyListeners(Notification notification, String event);
    void addListener(NotificationListener listener);
    void removeListener(NotificationListener listener);
}

interface NotificationListener{
    void onNotificationEvent(Notification notification, String event);
}

class NotificationPush implements NotificationPusher {

    private Map<DeliveryChannel, DeliveryStrategy> strategies;
    private List<NotificationListener> listeners;

    public NotificationPush(Map<DeliveryChannel, DeliveryStrategy> strategies) {

        this.strategies = new EnumMap<>(DeliveryChannel.class);
        this.strategies.putAll(strategies);

        this.listeners = new CopyOnWriteArrayList<>();
    }

    @Override
    public void pushNotification(Notification notification) {
        if (notification == null) {
            throw new IllegalArgumentException("Notification cannot be null");
        }
        if (notification.status == Status.SENT) {
            throw new IllegalStateException("Notification already sent");
        }
        // Notify observers about creation
        notifyListeners(notification, "CREATED");

        notification.status = Status.PENDING;
        notifyListeners(notification, "PENDING");

        try {
            // 1. Select appropriate Strategy
            DeliveryStrategy strategy = strategies.get(notification.channel);
            if (strategy == null) {
                throw new IllegalArgumentException("Unsupported delivery channel");
            }
            notification.status = Status.SENT;
            notifyListeners(notification, "SENT");
            // 2. Apply Retry Decorator if enabled
            if (notification.retryEnabled) {
                strategy = new RetryDecorator(strategy, 3);
            }
            // 3. Apply Priority Decorator if enabled
            if (notification.highPriority) {
                strategy = new PriorityDecorator(strategy);
            }

            // 4. Execute decorated delivery strategy
            boolean sent = strategy.sendNotification(notification);

            if (!sent) {
                throw new RuntimeException("Notification delivery failed");
            }
            // 5. Update status
            notification.status = Status.SENT;
            notifyListeners(notification, "SENT");

        } catch (RuntimeException e) {
            notification.status = Status.FAILED;
            notifyListeners(notification, "FAILED: " + e.getMessage());
        }
    }

    @Override
    public void notifyListeners(Notification notification, String event) {
        for (NotificationListener listener : listeners) {
            try {
                listener.onNotificationEvent(notification, event);
            } catch (RuntimeException e) {
                System.out.println("Observer failed: " + e.getMessage());
            }
        }
    }
    @Override
    public void addListener(NotificationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(NotificationListener listener) {
        listeners.remove(listener);
    }
}

class NotificationLogger implements NotificationListener{
    public void onNotificationEvent(Notification notification, String event){
        System.out.println("Notification Event: " + event + " for notification: " + notification.message);
    }
}

class NotificationAnalytics implements NotificationListener{
    public void onNotificationEvent(Notification notification, String event){
        System.out.println("Analytics: Notification Event: " + event + " for notification: " + notification.message);
    }
}

class NotificationAudit implements NotificationListener{
    public void onNotificationEvent(Notification notification, String event){
        System.out.println("Audit: Notification Event: " + event + " for notification: " + notification.message);
    }
}

