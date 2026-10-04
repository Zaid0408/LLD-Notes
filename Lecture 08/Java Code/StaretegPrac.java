/*
You need to calculate delivery charges.

There are different strategies:

Normal Delivery → ₹50
Express Delivery → ₹100
Premium Delivery → ₹150

Solves OCP and DIP as well

Why not just use inheritance?

This is an important question.

Suppose you did:

DeliveryService
   ├── NormalDeliveryService
   ├── ExpressDeliveryService
   └── PremiumDeliveryService

Now the delivery service itself is getting duplicated.

The actual difference between these classes is just the algorithm used to calculate the charge.

Strategy says:

Don't create different versions of the whole object just because one algorithm changes.

Instead:

DeliveryService
      +
DeliveryStrategy
*/

interface DeliveryStrategy {
    double calculateDeliveryCharges(double distance);
} // startegy interface , strategies will implement this interface in therir own way

/*
three strategies to implement the interface , NormalDelivery, ExpressDelivery, PremiumDelivery 
(charge delivery price based on what is selected and reduce time )

DeliveryService
      ↓
DeliveryStrategy
      ↑
      |
 ┌────┼───────────────┐
 ↓    ↓               ↓
Normal Express     Premium
*/

class NormalDelivery implements DeliveryStrategy {
    @Override
    public double calculateDeliveryCharges(double distance) {
        if(distance <= 10) {
            return 50;
        }
        return distance*0.2+ 60;
    }
}

class ExpressDelivery implements DeliveryStrategy {
    @Override
    public double calculateDeliveryCharges(double distance) {
        if(distance <= 10) {
            return 100;
        }
        return distance*0.2+ 110;
    }
}

class PremiumDelivery implements DeliveryStrategy {
    @Override
    public double calculateDeliveryCharges(double distance) {
        if(distance <= 10) {
            return 150;
        }
        return distance*0.2+ 160;
    }
}

class DeliveryService{
    private DeliveryStrategy deliveryStrategy;

    public DeliveryService(DeliveryStrategy deliveryStrategy) {
        this.deliveryStrategy = deliveryStrategy;
    }
    public int calculateDeliveryCharge(int distance) {
        return strategy.calculateCharge(distance);
    }

    public void setStrategy(DeliveryStrategy strategy) {
        this.strategy = strategy; // changing the strategy to be used by the client
    }
}

public class StaretegPrac {
    public static void main(String[] args) {
        DeliveryService deliveryService = new DeliveryService(new NormalDelivery() );

        System.out.println(deliveryService.calculateDeliveryCharge(5));

        deliveryService.setStrategy(new ExpressDelivery()); // change the strategy at run time 
        System.out.println(deliveryService.calculateDeliveryCharge(5));

        deliveryService.setStrategy(new PremiumDelivery());
        System.out.println(deliveryService.calculateDeliveryCharge(5));
        
    }
}