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
// Command interface
interface Command{
    void execute();
    void undo();
}

class AirConditionerCommand implements Command{
    private AirConditioner ac;
    
    public AirConditionerCommand(AirConditioner ac) {
        this.ac = ac;
    }

    public void execute() {
        ac.on();
    }

    public void undo() {
        ac.off();
    }
}

class LightsCommand implements Command{
    private Lights lights;

    public LightsCommand(Lights lights) {
        this.lights = lights;
    }

    public void execute() {
        lights.on();
    }

    public void undo() {
        lights.off();
    }
}

class TelevisionCommand implements Command{
    private Television tv;

    public TelevisionCommand(Television tv) {
        this.tv = tv;
    }

    public void execute() {
        tv.on();
    }

    public void undo() {
        tv.off();
    }
}

// Invoker class

class SmartHomeController{
    private Command acCommand;
    private Command lightsCommand;
    private Command tvCommand;

    public SmartHomeController(Command acCommand,Command lightsCommand, Command tvCommand){
        this.acCommand = acCommand;
        this.lightsCommand = lightsCommand;
        this.tvCommand = tvCommand;
    }

    public void startAllDevices(){
        acCommand.execute();
        lightsCommand.execute();
        tvCommand.execute();
    }

    public void startAC(){
        acCommand.execute();
    }
    public void startLights(){
        lightsCommand.execute();
    }
    public void startTV(){
        tvCommand.execute();
    }
    public void stopAllDevices(){
        acCommand.undo();
        lightsCommand.undo();
        tvCommand.undo();
    }
    public void stopAC(){
        acCommand.undo();
    }
    public void stopLights(){
        lightsCommand.undo();
    }
    public void stopTV(){
        tvCommand.undo();
    }

}

public class CommandPrac {
    public static void main(String[] args)
    {
        Command acCommand =new AirConditionerCommand(new AirConditioner());
        Command lightsCommand = new LightsCommand(new Lights());
        Command tvCommand = new TelevisionCommand(new Television());

        SmartHomeController controller = new SmartHomeController(acCommand,lightsCommand,tvCommand);
        controller.startAllDevices();
        controller.stopAllDevices();

        controller.startAC();
        controller.startLights();
        controller.stopAC();
        controller.stopTV();
        controller.stopLights();
    }
}
