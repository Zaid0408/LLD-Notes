
/*
Encapsulation says 2 things:
1. An Object's Characteristics and its behaviour are encapsulated together
within that Object.
2. All the characteristics or behaviours are not for everyone to access.
Object should provide data security.

We follow above 2 pointers about Object of real world in programming by:
1. Creating a class that act as a blueprint for Object creation. Class contain
all the characteristics (class variable) and behaviour (class methods) in one block,
encapsulating it together.
2. We introduce access modifiers (public, private, protected, default) etc to provide data
security to the class members.
*/
class SportsCar {
    private String brand;
    private String model;
    private boolean isEngineOn = false;
    private int currentSpeed = 0;
    private int currentGear = 0;
        
    //Introduce new variable to exaplain setters
    private String tyreCompany;

    public SportsCar(String brand, String model) {
        this.brand = brand;
        this.model = model;
    }

    public int getSpeed() {
        return currentSpeed;
    }

    public String getTyreCompany() {
        return tyreCompany;
    }

    public void setTyreCompany(String tyreCompany) {
        this.tyreCompany = tyreCompany;
    }

    public void startEngine() {
        isEngineOn = true;
        System.out.println(brand + " " + model + " : Engine starts with a roar!");
    }

    public void shiftGear(int gear) {
        this.currentGear = gear;
        System.out.println(brand + " " + model + " : Shifted to gear " + currentGear);
    }

    public void accelerate() {
        if (!isEngineOn) {
            System.out.println(brand + " " + model + " : Engine is off! Cannot accelerate.");
            return;
        }
        currentSpeed += 20;
        System.out.println(brand + " " + model + " : Accelerating to " + currentSpeed + " km/h");
    }
    
    public void brake() {
        currentSpeed -= 20;
        if (currentSpeed < 0) currentSpeed = 0;
        System.out.println(brand + " " + model + " : Braking! Speed is now " + currentSpeed + " km/h");
    }

    public void stopEngine() {
        isEngineOn = false;
        currentGear = 0;
        currentSpeed = 0;
        System.out.println(brand + " " + model + " : Engine turned off.");
    }
}

//Main Method
public class Encapsulation {
    public static void main(String[] args) {

        // SportsCar mySportsCar = new SportsCar("Ford", "Mustang");

        // mySportsCar.startEngine();
        // mySportsCar.shiftGear(1);
        // mySportsCar.accelerate();
        // mySportsCar.shiftGear(2);
        // mySportsCar.accelerate();
        // mySportsCar.brake();
        // mySportsCar.stopEngine();

        //Setting arbitrary value to speed.
        //mySportsCar.currentSpeed = 500;

       // System.out.println("Current Speed of My Sports Car is set to " + mySportsCar.currentSpeed);

       // System.out.println("Current Speed of My Sports Car is " + mySportsCar.getSpeed());

       MyAccount myAccount = new MyAccount(1000);
       myAccount.deposit(500);
       myAccount.withdraw(200);

       System.out.println("Current Balance of My Account is " + myAccount.getBalance());
    }
} 

class MyAccount{
    private long balance;


    public MyAccount(long balance){
        this.balance = balance;
    }

    public void setBalance(long balance){
        this.balance = balance;
    }

    public long getBalance(){
        return balance;
    }
    public void deposit(long amount){
        balance += amount;
        System.out.println("Deposited: " + amount + " in Current Account. New Balance: " + balance);
    }

    public long withdraw(long amount)
    {
        if(balance >= amount)
        {
            balance -= amount;
            System.out.println("Withdrawn: " + amount + " from Current Account. New Balance: " + balance);
        }
        else
        {
            System.out.println("Insufficient funds in Current Account!");
            return -1;
        }
        return balance;
    }
}
