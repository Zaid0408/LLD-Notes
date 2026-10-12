/*
Thread-safe Eager initialization
Terminology: Eager Initialization Singleton. Why eager?

Because the object is created as soon as the JVM initializes the class, regardless of whether anyone has called getInstance().
Why is this thread-safe?

Java guarantees that class initialization is synchronized.
This means if multiple threads try to initialize the same class simultaneously, only one thread performs the initialization. Other threads wait until initialization completes.

Therefore, only one instance is created.
Important: This is thread-safe without explicitly using synchronized.
*/

public class ThreadSafeEagerSingleton {
    private static ThreadSafeEagerSingleton instance = new ThreadSafeEagerSingleton();
    // object is created as soon as the JVM initializes the class, regardless of whether anyone has called getInstance().
    private ThreadSafeEagerSingleton() {
        System.out.println("Singleton Constructor Called!");
    }

    public static ThreadSafeEagerSingleton getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        ThreadSafeEagerSingleton s1 = ThreadSafeEagerSingleton.getInstance();
        ThreadSafeEagerSingleton s2 = ThreadSafeEagerSingleton.getInstance();

        System.out.println(s1 == s2);
    }
}
