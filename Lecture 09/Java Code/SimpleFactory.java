// --- Burger Interface ---
interface Burger {
    void prepare();
}

// --- Concrete Burger Implementations ---
class BasicBurger implements Burger {
    @Override
    public void prepare() {
        System.out.println("Preparing Basic Burger with bun, patty, and ketchup!");
    }
}

class StandardBurger implements Burger {
    @Override
    public void prepare() {
        System.out.println("Preparing Standard Burger with bun, patty, cheese, and lettuce!");
    }
}

class PremiumBurger implements Burger {
    @Override
    public void prepare() {
        System.out.println("Preparing Premium Burger with gourmet bun, premium patty, cheese, lettuce, and secret sauce!");
    }
}

// --- Burger Factory ---
class BurgerFactory {
    public Burger createBurger(String type) {
        if (type.equalsIgnoreCase("basic")) {
            return new BasicBurger();
        } else if (type.equalsIgnoreCase("standard")) {
            return new StandardBurger();
        } else if (type.equalsIgnoreCase("premium")) {
            return new PremiumBurger();
        } else {
            System.out.println("Invalid burger type!");
            return null;
        }
    }
}

// --- Main Class ---
public class SimpleFactory {
    public static void main(String[] args) {
        String type = "standard";

        BurgerFactory myBurgerFactory = new BurgerFactory();

        Burger burger = myBurgerFactory.createBurger(type);

        if (burger != null) {
            burger.prepare();
        }

        type="email";
        NotificationFactory notificationFactory=new NotificationFactory();
        Notification notification=notificationFactory.getNotification(type);
        notification.sendNotification("Hello");

        type="push";
        notification=notificationFactory.getNotification(type);
        notification.sendNotification("Hello");
    }
}

// Practice Problem 

/*

Notification System Build a notification system supporting:

Email

SMS

Push Notification

Requirements:

Create a Notification interface.
Implement the three notification types.
Create a NotificationFactory that accepts a notification type and returns the appropriate object.
The client should use the returned notification without knowing its concrete implementation.

Goal: Understand how to separate object creation from client code.
*/

interface Notification{
    public void sendNotification(String message);
}

class EmailNotification implements Notification{
    public void sendNotification(String message){
        System.out.println("Email Notification sent"+ "\nMessage: " + message);
    }
}

class SMSNotification implements Notification{
    public void sendNotification(String message){
        System.out.println("SMS Notification sent" + "\nMessage: " + message);
    }
}

class PushNotification implements Notification{
    public void sendNotification(String message){
        System.out.println("Push Notification sent"+ "\nMessage: " + message);
    }
}

class NotificationFactory{
    public Notification getNotification(String type){
        switch(type)
        {
            case "email":
                return new EmailNotification();
            case "sms":
                return new SMSNotification();
            case "push":
                return new PushNotification();
            default:
                return null;
        }
    }
}

class PracticeMain{
    public static void main (String[] args){
        String type="email";
        NotificationFactory notificationFactory=new NotificationFactory();
        Notification notification=notificationFactory.getNotification(type);
        notification.sendNotification("Hello");

        type="push";
        notification=notificationFactory.getNotification(type);
        notification.sendNotification("Hello");
    }
}
