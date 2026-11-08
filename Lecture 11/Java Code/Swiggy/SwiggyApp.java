// Swiggy food delivery app implementation : Similar to the tomato food implementation 

// Entities
import java.util.*;
class Customer {
    private long id;
    private String name;
    private int number;

    Customer(long id,String name,int number)
    {
        this.id=id;
        this.name=name;
        this.number=number;
    }
    long getId()
    {
        return this.id;
    }
    String getName()
    {
        return this.name;
    }
    int getNumber()
    {
        return this.number;
    }
}

enum OrderStatus{
    CREATED,PENDING,SUCCESS,FAIL
}

enum PaymentMethod{
    UPI,CREDIT_CARD
}

class MenuItem{
    private String foodName,description;
    private int price;
    MenuItem(String foodName,String description,int price)
    {
        this.foodName=foodName;
        this.description=description;
        this.price=price;
    }
    int getPrice()
    {
        return this.price;
    }
    String getFoodName()
    {
        return this.foodName;
    }
}

class OrderItem{
    MenuItem menuItem;
    int quantity;
    OrderItem(MenuItem menuItem,int quantity)
    {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        this.menuItem=menuItem;
        this.quantity=quantity;
    }
    void updateQuantity(int quantity)
    {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.quantity=quantity;
    }
    int calcOrderItemprice()
    {
        return this.quantity * this.menuItem.getPrice();
    }
    MenuItem getMenuItem() {
        return menuItem;
    }
    int getQuantity() {
        return quantity;
    }
}
class Cart{
    List<OrderItem> items;
    long restaurantId;
    Cart(long restaurantId)
    {
        this.items=new ArrayList<>();
        this.restaurantId=restaurantId;
    }
    void addItem(OrderItem item)
    {
        this.items.add(item);
    }
    void updateItemQuantity(OrderItem item,int quantity)
    {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        for(OrderItem orderItem:items)
        {
            if(orderItem==item)
            {
                orderItem.updateQuantity(quantity);
                return;
            }
        }
        throw new RuntimeException("Item not found in cart");
    }
    void removeItem(OrderItem item)
    {
        this.items.remove(item);
    }
    int getCartTotal()
    {
        int total=0;
        for(OrderItem item:items)
        {
            total+=item.calcOrderItemprice();
        }
        return total;
    }

    void clearCart()
    {
        this.restaurantId=0;
        this.items.clear();
    }
}
/*
Cart Temporary / Volatile. Items can be added, removed, or changed at any time.	
Order: Permanent / Immutable. Once placed, it cannot change. If you cancel, it becomes "Cancelled", not edited.
Hence Both Cart and Order are different entities. Cart is a temporary collection of items, while Order is a permanent record of a purchase.
And both are required 
One Cart in One restaurant. One Order in One Restaurant.
*/

class Order{
    private long orderId;
    private Customer customer;
    private final List<OrderItem> items; // This is final immutable and cannot be edited 
    private OrderStatus status;
    private long restaurantId;
    Order(long orderId,Customer customer,List<OrderItem> items, long restaurantId)
    {
        this.orderId=orderId;
        this.customer=customer;
        this.items = new ArrayList<>();
        for (OrderItem item : items) { // deep copy of the items to ensure immutability
            this.items.add(new OrderItem(item.getMenuItem(), item.getQuantity()));
        } // this is very important
        // This is done so that the Order has its own copy of the items and cannot be affected by changes to the original list in the Cart.
        // If this is not done, then the Order's items would be a reference to the same list in the Cart, and any changes to the Cart would affect the Order, which is not desired.
        this.status=OrderStatus.CREATED;
        this.restaurantId=restaurantId;
    }
    void updateStatus(OrderStatus status)
    {
        this.status=status;
    }
    OrderStatus getStatus()
    {
        return this.status;
    }
    long getOrderId()
    {
        return this.orderId;
    }
}

class Restaurant{
    private long restaurantId;
    private String name;
    private List<MenuItem> menuItems;
    Restaurant(long restaurantId,String name)
    {
        this.restaurantId=restaurantId;
        this.name=name;
        this.menuItems=new ArrayList<>();
    }
    long getRestaurantId(){ return this.restaurantId; }
    String getName(){ return this.name; }
    List<MenuItem> getMenuItems(){ return this.menuItems; }

    void addMenuItem(MenuItem item)
    {
        this.menuItems.add(item);
    }
}

class RestaurantManager{
    private final static RestaurantManager instance=new RestaurantManager();
    private RestaurantManager() {

    }
    public static RestaurantManager getInstance(){
        return instance;
    }
    private final Map<Long,Restaurant> restaurants=new HashMap<>();
    void addRestaurant(Restaurant restaurant)
    {
        this.restaurants.put(restaurant.getRestaurantId(),restaurant);
    }
    Restaurant getRestaurant(long restaurantId)
    {
        return this.restaurants.get(restaurantId);
    }
    Restaurant getRestaurantByName(String name)
    {
        for(Restaurant restaurant:restaurants.values())
        {
            if(restaurant.getName().equals(name))
                return restaurant;
        }
        return null;
    }
}

// Strategy pattern for payments 
// remember that methods have to be public because PaymentProcessor must access them 

interface PaymentStrategy {
    boolean pay(int amount);
}

class UPIPayment implements PaymentStrategy {
    private String upiId;
    public UPIPayment(String upiId) {
        this.upiId = upiId;
    }
    @Override
    public boolean pay(int amount) {
        System.out.println("Processing UPI payment of " + amount + " using UPI ID: " + upiId);
        return true; // Assume payment is successful
    }
}

class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;
    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    @Override
    public boolean pay(int amount) {
        System.out.println("Processing credit card payment of " + amount + " using card: " + cardNumber);
        return true; // Assume payment is successful
    }
}

class PaymentProcessor {
    private PaymentStrategy paymentStrategy;
    public PaymentProcessor(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
    public boolean processPayment(int amount) {
        return paymentStrategy.pay(amount);
    }
}


// Simple Notification Service for sending notifications to customers
// This is to fulfil the need pf Non functional Requirement 
// Of sending the notification to the customer about the order status and if order is placed or not 
class NotificationService{
    public void sendNotification(Customer customer, String message) {
        if(customer.getNumber()==0)
        {
            System.out.println("Customer has no number, cannot send notification");
            return;
        }
        System.out.println("Sending notification to " + customer.getName() + ": " + message);
    }
}

class CheckoutService{
    private PaymentProcessor paymentProcessor;
    private NotificationService notificationService;
    public CheckoutService(PaymentProcessor paymentProcesser, NotificationService notificationService)
    {
        this.paymentProcessor=paymentProcesser;
        this.notificationService=notificationService;
    }
    public void checkout(Customer cust, Cart cart, long restaurantId)
    {
        if (cart == null || cart.getCartTotal() <= 0) {
            throw new IllegalArgumentException("Cart is empty");
        }
        int amount= cart.getCartTotal();
        boolean payment=paymentProcessor.processPayment(amount);
        if(!payment)
        {
            throw new RuntimeException("Payment failed");
        }
        // Create Order
        long orderId=System.currentTimeMillis(); // simple way to generate unique order id
        Order order = new Order(orderId,cust,cart.items,restaurantId);
        System.out.println("Order placed successfully with order id: " + orderId + "with status: " + order.getStatus());
        order.updateStatus(OrderStatus.PENDING);
        System.out.println("Order status updated to: " + order.getStatus());
        order.updateStatus(OrderStatus.SUCCESS);

        String message = "Your order with id " + orderId + " has been placed successfully. Current status: " + order.getStatus();
        notificationService.sendNotification(cust, message);

    }
}

public class SwiggyApp {
    public static void main(String[] args) {
        
        // Create a restaurant and add menu items
        Restaurant restaurant = new Restaurant(1, "Pizza Place");
        restaurant.addMenuItem(new MenuItem("Margherita Pizza", "Classic cheese pizza", 10));
        restaurant.addMenuItem(new MenuItem("Pepperoni Pizza", "Spicy pepperoni pizza", 12));
        restaurant.addMenuItem(new MenuItem("Barbeque Pizza", "Loaded with vegetables", 11));

        Restaurant restaurant2 = new Restaurant(2, "Burger King");
        restaurant2.addMenuItem(new MenuItem("Classic Burger", "Classic cheese burger", 10));
        restaurant2.addMenuItem(new MenuItem("Spicy Chicken Burger", "Spicy chicken burger", 12));
        restaurant2.addMenuItem(new MenuItem("Veggie Burger", "Loaded with vegetables", 11));

        // Register the restaurant with the manager
        RestaurantManager.getInstance().addRestaurant(restaurant);
        RestaurantManager.getInstance().addRestaurant(restaurant2);

        // Create a customer
        Customer customer = new Customer(1, "Alice", 1234567890);

        // Search for the restaurant by name
        Restaurant searchedRestaurant = RestaurantManager.getInstance().getRestaurantByName("Pizza Place");

        // Create a cart for the customer
        Cart cart = new Cart(searchedRestaurant.getRestaurantId());
        OrderItem item1 = new OrderItem(searchedRestaurant.getMenuItems().get(0), 2); // 2 Margherita Pizzas
        OrderItem item2 = new OrderItem(searchedRestaurant.getMenuItems().get(1), 1); // 1 Pepperoni Pizza
        OrderItem item3 = new OrderItem(searchedRestaurant.getMenuItems().get(2), 1); // 1 Barbeque
        cart.addItem(item1); // 2 Margherita Pizzas
        cart.addItem(item2); // 1 Pepperoni Pizza
        cart.addItem(item3); // 1 Barbeque Pizza
        cart.updateItemQuantity(item1, 1); // Update to 1 Margherita Pizzas

        // Choose a payment method (UPI in this case)
        PaymentStrategy paymentStrategy = new UPIPayment("alice@upi");
        PaymentProcessor paymentProcessor = new PaymentProcessor(paymentStrategy);

        // Create a notification service
        NotificationService notificationService = new NotificationService();

        // Create a checkout service
        CheckoutService checkoutService = new CheckoutService(paymentProcessor, notificationService);

        // Perform checkout
        try {
            checkoutService.checkout(customer, cart, searchedRestaurant.getRestaurantId());
        } catch (RuntimeException e) {
            System.out.println("Checkout failed: " + e.getMessage());
        }

        cart.clearCart(); // Clear the cart after checkout
    }
}
