// Entities
import java.util.*;

class Product{
    String id,name;
    int stock;
    long price;

    Product(String id,String name,int stock,long price){
        this.id=id;
        this.name=name;
        this.stock=stock;
        this.price=price;
    }

    public long getPrice(){
        return price;
    }

    public int reduceStock(int quantity){
        stock-=quantity;
        return stock;
    }
    public int getStock(){
        return stock;
    }
}


// in correct implementation
// class Order{ // might need to segregate order and Order Item as Order items will be needing a status as well 
//     String orderId,customerName;
//     List<Product> items=new ArrayList<>();
//     public enum Status{PENDING,SHIPPED,DELIVERED,CANCELLED}
//     Status status;

//     long orderAmount;
//     public Order(String orderId,String customerName,List<Product> items,Status status){
//         this.orderId=orderId;
//         this.customerName=customerName;
//         this.items=items;
//         this.status=status;
//     }
//     long calculateOrderAmount(){
//         orderAmount=0;
//         for(Product p:items){
//             orderAmount+=p.price;
//         }
//         return orderAmount;
//     }
// }

// correct implementation 

class OrderItem{
    Product product;
    int quantity;
    public OrderItem(Product product,int quantity){
        this.product=product;
        this.quantity=quantity;
    }

    long calculateItemAmount(){
        return product.price*quantity;
    }
}

class Order{
    String orderId,customerName;
    List<OrderItem> items=new ArrayList<>();
    long orderAmount;
    public Order(String orderId,String customerName,List<OrderItem> items){
        this.orderId=orderId;
        this.customerName=customerName;

    }
    long calculateOrderAmount(){
        orderAmount=0;
        for(OrderItem o:items){
            orderAmount+=o.calculateItemAmount();
        }
        return orderAmount;
    }
}

interface Discount{
    public long addDiscount(long amount);
}

class PercentageDiscount implements Discount{
    @Override
    public long addDiscount(long amount) {
        if(amount<500)
        {
            throw new ImplementationException("Cannot Apply Percentage Discount");
            return 0;
        }
        return amount-amount*0.1;
    }
}

class FlatDiscount implements Discount{
    @Override
    public long addDiscount(long amount) {
        if(amount<1000)
        {
            throw new ImplementationException("Cannot Apply Flat Discount");
            return 0;
        }
        return amount-500;
    }
}

// payment
interface PaymentMethod{
    public boolean makePayment(long amount);
}

class CreditCardPayment implements PaymentMethod{
    @Override
    public boolean makePayment(long amount) {
        System.out.println("Payment of Rs" + amount + " made using Credit Card");
        System.out.println("Payment Successful");
        return true;
    }
}

class UPIPayment implements PaymentMethod{
    @Override
    public boolean makePayment(long amount) {
        System.out.println("Payment of Rs" + amount + " made using UPI");
        System.out.println("Payment Successful");
        return 
    }
}

interface Notification{
    public void sendNotification(String message);
}

class EmailNotification implements Notification{
    String emailId;
    public EmailNotification(String emailId){
        this.emailId=emailId;
    }
    @Override
    public void sendNotification(String message) {
        System.out.println("Email Notification sent to " + emailId + "\nMessage: " + message);
    }
}

class SMSNotification implements Notification{
    String mobileNumber;
    public SMSNotification(String mobileNumber){
        this.mobileNumber=mobileNumber;
    }
    @Override
    public void sendNotification(String message) {
        System.out.println("SMS Notification sent to " + mobileNumber + "\nMessage: " + message);
    }
}



public class Ecommerce {
    public boolean checkout(Order order,Discount discount, PaymentMethod paymentMethod, Notification notification) {
        // 1. Check inventory
        for (OrderItem item : order.items) {

            if (item.product.stock < item.quantity) {
                System.out.println(
                    "Insufficient stock for " + item.product.name
                );

                return false;
            }
        }

        // 2. Calculate order amount
        long amount = order.calculateOrderAmount();

        // 3. Apply discount
        long finalAmount = discount.applyDiscount(amount);

        System.out.println("Original Amount: Rs " + amount);
        System.out.println("Final Amount: Rs " + finalAmount);

        // 4. Make payment
        boolean paymentSuccessful = paymentMethod.makePayment(finalAmount);

        if (!paymentSuccessful) {
            System.out.println("Payment Failed");
            return false;
        }

        // 5. Update inventory
        for (OrderItem item : order.items) {
            item.product.stock -= item.quantity;
        }
        // 7. Notification
        notification.sendNotification("Order " + order.orderId + " placed successfully.");

        return true;
    }
}
