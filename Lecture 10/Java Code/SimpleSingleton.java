/*
Singleton Pattern:

1. Ensures that a class has only one instance.
2. Provides a global access point to that instance.
3. Uses a private constructor to prevent external object creation.
4. Uses a static instance variable to maintain the single object.
5. Uses a public static method to provide access to it.

Eager Initialization:
Instance is created when the class is initialized.

Thread Safety:
Java class initialization guarantees thread-safe creation
for this implementation.
*/


/*
Not thread-safe Lazy initialization,
Terminology: Lazy Initialization Singleton (without synchronization).Why lazy?

Because the instance is created only when someone calls getInstance() for the first time.

What's the problem?

This implementation is not thread-safe.Imagine two threads calling getInstance() at almost the same time:

Thread 1                  Thread 2
   |                         |
   | getInstance()            | getInstance()
   |                         |
   | instance == null         | instance == null
   |                         |
   | new Object A             | new Object B
   |                         |
   ↓                         ↓
  Object A                  Object B

Both threads can see instance == null and create separate objects.
So your implementation works in a single-threaded environment but does not guarantee a Singleton under concurrent access.
*/

public class SimpleSingleton {
    private static SimpleSingleton instance = null;

    private SimpleSingleton() {
        System.out.println("Singleton Constructor called");
    }

    public static SimpleSingleton getInstance() {
        if (instance == null) {
            instance = new SimpleSingleton();
        }
        return instance;
    }

    public static void main(String[] args) {
        SimpleSingleton s1 = SimpleSingleton.getInstance();
        SimpleSingleton s2 = SimpleSingleton.getInstance();

        System.out.println(s1 == s2);
    }
}
