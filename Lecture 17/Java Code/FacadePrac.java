/*
Smart Home Controller
Build a simple Smart Home system.

Your system contains:
- Air Conditioner
- Lights
- Television
Each device has its own operations.

The user should be able to perform one operation to start the entire Smart Home.

Requirements:
1. Create separate classes for the three devices.
2. Each device should have its own start operation.
3. Implement An object between these devices and the user so as to do the operations on the devices without the user knowing the details of the devices.
4. A class that should be able to perform the opeartions on the devices without the user knowing the details of the devices.

Constraint: Keep the implementation simple. No database, interfaces beyond what you actually need, or unnecessary abstractions.
*/

// ----------------------------
// Receivers
// ----------------------------
class AirConditioner {
    boolean isOn = false;
    public void on() {
        if(isOn) {
            System.out.println("Air Conditioner is already on.");
            return;
        }
        isOn = true;
        System.out.println("Air Conditioner is switching on...");
    }
    public void off() {
        if(!isOn) {
            System.out.println("Air Conditioner is already off.");
            return;
        }
        isOn = false;
        System.out.println("Air Conditioner is switching off...");
    }
}

class Lights {
    boolean isOn = false;
    public void on() {
        if(isOn) {
            System.out.println("Lights are already on.");
            return;
        }
        isOn = true;
        System.out.println("Lights are switching on...");
    }
    public void off() {
        if(!isOn) {
            System.out.println("Lights are already off.");
            return;
        }
        isOn = false;
        System.out.println("Lights are switching off...");
    }
}

class Television {
    boolean isOn = false;
    public void on() {
        if(isOn) {
            System.out.println("Television is already on.");
            return;
        }
        isOn = true;
        System.out.println("Television is switching on...");
    }
    public void off() {
        if(!isOn) {
            System.out.println("Television is already off.");
            return;
        } 
        isOn = false;
        System.out.println("Television is switching off...");
        
    }
}

class SmartHomeControllerFacade{
    private AirConditioner ac;
    private Lights lights;
    private Television tv;

    public SmartHomeControllerFacade() {
        this.ac = new AirConditioner();
        this.lights = new Lights();
        this.tv = new Television();
    }

    public void startSmartHome() {
        System.out.println("Starting Smart Home...");
        ac.on();
        lights.on();
        tv.on();
        System.out.println("Smart Home is ready!");
    }

    public void stopSmartHome() {
        System.out.println("Stopping Smart Home...");
        ac.off();
        lights.off();
        tv.off();
        System.out.println("Smart Home is stopped!");
    }
}


public class FacadePrac {
    public static void main(String[] args)
    {
        SmartHomeControllerFacade smartHome= new SmartHomeControllerFacade();
        smartHome.startSmartHome();
        smartHome.stopSmartHome();
    }
}
