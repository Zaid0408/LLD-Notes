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
    
}

enum Status{
    CREATED,PENDING,SENT,FAILED;
}
enum DeliveryChannel{
    EMAIL,SMS,PUSH
}
class Notification{
    long id;
    Status status;
    DeliveryChannel channel;
    String message;
    Instant createdAt;
}


