// Component Interface: defines a common interface for Mario and all power-up decorators.
interface Character {
    String getAbilities();
}

// Concrete Component: Basic Mario character with no power-ups.
class Mario implements Character {
    public String getAbilities() {
        return "Mario";
    }
}

// Abstract Decorator: CharacterDecorator "is-a" Character and "has-a" Character.
/*
This is the most important part.
It implements the same interface as the original object and maintains a reference to another object of that interface.
Notice: protected Character character;
This means the decorator HAS-A reference to another Character.
And:
implements Character
means the decorator IS-A Character.

This combination is the foundation of the Decorator Pattern.
*/
abstract class CharacterDecorator implements Character {
    protected Character character;  // Wrapped component

    public CharacterDecorator(Character c) {
        this.character = c;
    }
}

// Concrete Decorator: Height-Increasing Power-Up. These are the actual classes that add new functionality.
class HeightUp extends CharacterDecorator {
    public HeightUp(Character c) {
        super(c);
    }

    public String getAbilities() {
        return character.getAbilities() + " with HeightUp";
    }
    // It does two things:
    // 1. Calls getAbilities() on the existing character.
    // 2. Adds its own functionality to the result.
}

// Concrete Decorator: Gun Shooting Power-Up.
class GunPowerUp extends CharacterDecorator {
    public GunPowerUp(Character c) {
        super(c);
    }

    public String getAbilities() {
        return character.getAbilities() + " with Gun";
    }
}

// Concrete Decorator: Star Power-Up (temporary ability).
class StarPowerUp extends CharacterDecorator {
    public StarPowerUp(Character c) {
        super(c);
    }

    public String getAbilities() {
        return character.getAbilities() + " with Star Power (Limited Time)";
    }
}

public class DecoratorPattern {
    public static void main(String[] args) {
        // Create a basic Mario character.
        Character mario = new Mario();
        System.out.println("Basic Character: " + mario.getAbilities());

        // Decorate Mario with a HeightUp power-up.
        mario = new HeightUp(mario);
        System.out.println("After HeightUp: " + mario.getAbilities()); // the getAbilities() method is called on the HeightUp decorator, which in turn calls getAbilities() on the original Mario object and adds its own functionality.

        // Decorate Mario further with a GunPowerUp.
        mario = new GunPowerUp(mario);
        System.out.println("After GunPowerUp: " + mario.getAbilities()); // the getAbilities() method is called on the GunPowerUp decorator, which in turn calls getAbilities() on the HeightUp decorator, which in turn calls getAbilities() on the original Mario object and adds its own functionality.

        // Finally, add a StarPowerUp decoration.
        mario = new StarPowerUp(mario);
        System.out.println("After StarPowerUp: " + mario.getAbilities()); // the getAbilities() method is called on the StarPowerUp decorator, which in turn calls getAbilities() on the GunPowerUp decorator, which in turn calls getAbilities() on the HeightUp decorator, which in turn calls getAbilities() on the original Mario object and adds its own functionality.
    
        // Why does this work? Because each decorator wraps the previous one, creating a chain of responsibility. 
        // Each decorator adds its own behavior while delegating to the wrapped object for existing behavior. 
        // This allows for flexible and dynamic composition of behaviors at runtime without modifying the original classes.
    }
}
