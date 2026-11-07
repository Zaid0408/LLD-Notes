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

public class NoficationNewSystem {
    int k=0;

    public static void main(String[] args) {
        // Entry point for the notification engine.
    }
}

class NotificationStore {
    private static final NotificationStore INSTANCE = new NotificationStore();

    private final Map<Long, Recipient> recipients = new HashMap<>();
    private final Map<Long, Notification> notifications = new HashMap<>();

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
    Notification(long id,Status st,String message, long recipientId)
    {
        this.id=id;this.status=st;this.message=message;
        this.recipientId=recipientId;
        this.createdAt=Instant.now();
    }
}

// Decorator pattern for notification enhancements
interface Enhancement{
    String getNotification(long notificationId);
}
class NotificationLog implements Enhancement{
    public String getNotification(long notificationId)
    {
        Notification notification=NotificationStore.getInstance()
            .findNotification(notificationId).orElse(null);
        if(notification==null)
            throw new RuntimeException("Notification not found");
        return "Notification ID: "+notification.id+" Status: "+notification.status+" Message: "+notification.message+" Recipient ID: "+notification.recipientId+" Created At: "+notification.createdAt;
    }
}

abstract class NotificationDecorator implements Enhancement{
    protected Enhancement enhancement;
    public NotificationDecorator(Enhancement enhancement)
    {
        this.enhancement=enhancement;
    }
}

class RetryMechanism extends NotificationDecorator{
    public RetryMechanism(Enhancement enhancement)
    {
        super(enhancement);
    }
    public String getNotification(long notificationId)
    {
        String log=enhancement.getNotification(notificationId);
        Notification notification=NotificationStore.getInstance()
            .findNotification(notificationId).orElse(null);
        if(notification==null)
            throw new RuntimeException("Notification not found");
        if(notification.status==Status.FAILED)
        {
            System.out.println("Retrying notification ID: "+notification.id);
            // Simulate retry logic
            notification.status=Status.PENDING;
            log+="\nRetrying notification. New Status: "+notification.status;
        }
        return log;
    }
}

class HighPriorityNotification extends NotificationDecorator{
    public HighPriorityNotification(Enhancement enhancement)
    {
        super(enhancement);
    }
    public String getNotification(long notificationId)
    {
        String log=enhancement.getNotification(notificationId);
        Notification notification=NotificationStore.getInstance()
            .findNotification(notificationId).orElse(null);
        if(notification==null)
            throw new RuntimeException("Notification not found");
        System.out.println("High priority notification ID: "+notification.id);
        log+="\nHigh priority notification.";
        return log;
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
}

interface NotificationListener{
    void onNotificationEvent(Notification notification, String event);
}

class NotificationPush implements NotificationPusher{
    EmailNotification emailNotification;
    SMSNotification smsNotification;
    PushNotification pushNotification;
    List<NotificationListener> listeners;
    public NotificationPush(){
        emailNotification=new EmailNotification();
        smsNotification=new SMSNotification();
        pushNotification=new PushNotification();
        listeners=new ArrayList<>();
    }

    public void pushNotification(Notification notification)
    {
        boolean sent=false;
        try{
            switch(notification.channel)
            {
                case EMAIL:
                    sent=emailNotification.sendNotification(notification);
                    break;
                case SMS:
                    sent=smsNotification.sendNotification(notification);
                    break;
                case PUSH:
                    sent=pushNotification.sendNotification(notification);
                    break;
            }
            if(sent)
            {
                notification.status=Status.SENT;
                notifyListeners(notification,"SENT");
            }
        }
        catch(Exception e)
        {
            notification.status=Status.FAILED;
            notifyListeners(notification,"FAILED");
        }
    }

    public void notifyListeners(Notification notification, String event)
    {
        for(NotificationListener listener:listeners)
        {
            listener.onNotificationEvent(notification,event);
        }
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

