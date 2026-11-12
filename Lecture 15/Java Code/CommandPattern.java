// ----------------------------
// Command Interface
// ----------------------------
interface Command {
    void execute();
    void undo();
}

// ----------------------------
// Receivers
// ----------------------------
class Light {
    public void on()  {
        System.out.println("Light is ON");
    }
    public void off() {
        System.out.println("Light is OFF");
    }
}

class Fan {
    public void on()  {
        System.out.println("Fan is ON");
    }
    public void off() {
        System.out.println("Fan is OFF");
    }
}

// ----------------------------
// Concrete Command for Light
// ----------------------------
class LightCommand implements Command {
    private Light light;

    public LightCommand(Light l) {
        this.light = l;
    }

    public void execute() {
        light.on();
    }

    public void undo() {
        light.off();
    }
}

// ----------------------------
// Concrete Command for Fan
// ----------------------------
class FanCommand implements Command {
    private Fan fan;

    public FanCommand(Fan f) {
        this.fan = f;
    }

    public void execute() {
        fan.on();
    }

    public void undo() {
        fan.off();
    }
}

// ----------------------------
// Invoker: Remote Controller with static array of 4 buttons
// ----------------------------

/*
Your RemoteController is the Invoker.
private Command[] buttons;
It stores Command objects instead of directly storing references to Light, Fan, etc.

When someone presses a button: buttons[idx].execute();
The remote doesn't need to know whether it's controlling a light or fan. It simply executes the assigned command.
*/
class RemoteController {
    private static final int numButtons = 4;
    private Command[] buttons; // store Number of buttons and their assigned commands
    private boolean[] buttonPressed;
    // track the state of each button (on/off) this will help us identify if the button is pressed or not and we can toggle the state of the button when it is pressed again.
    public RemoteController() {
        buttons = new Command[numButtons];
        buttonPressed = new boolean[numButtons];
        for (int i = 0; i < numButtons; i++) {
            buttons[i] = null;
            buttonPressed[i] = false;  // false = off, true = on
        }
    }

    public void setCommand(int idx, Command cmd) {
        if (idx >= 0 && idx < numButtons) {
            buttons[idx] = cmd;
            buttonPressed[idx] = false;
        }
    }

    /*
    idx: the button index (0 to 3)  When a button is pressed, we check if it has an assigned command.
    If it does, we check the current state of the button (on/off).
    */
    public void pressButton(int idx) {
        if (idx >= 0 && idx < numButtons && buttons[idx] != null) { //validate the idx and check if the button has an assigned command
            if (!buttonPressed[idx]) { // if button is not pressed, execute the command
                buttons[idx].execute();
            } else {
                buttons[idx].undo(); // if button is already pressed, undo the command
            }
            buttonPressed[idx] = !buttonPressed[idx]; // toggle the state of the button
        } else {
            System.out.println("No command assigned at button " + idx);
        }
    }
}

// ----------------------------
// Main Application
// ----------------------------
public class CommandPattern {
    public static void main(String[] args) {
        Light livingRoomLight = new Light();
        Fan ceilingFan = new Fan();

        RemoteController remote = new RemoteController();

        remote.setCommand(0, new LightCommand(livingRoomLight));
        remote.setCommand(1, new FanCommand(ceilingFan));
        // Buttons array is set up with LightCommand at index 0 and FanCommand at index 1. The other two buttons remain unassigned.

        // Simulate button presses (toggle behavior)
        System.out.println("--- Toggling Light Button 0 ---");
        remote.pressButton(0);  // ON
        remote.pressButton(0);  // OFF

        System.out.println("--- Toggling Fan Button 1 ---");
        remote.pressButton(1);  // ON
        remote.pressButton(1);  // OFF

        // Press unassigned button to show default message
        System.out.println("--- Pressing Unassigned Button 2 ---");
        remote.pressButton(2);
    }
}
