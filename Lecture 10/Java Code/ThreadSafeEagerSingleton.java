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
    // diff between single and thread safe is here:
    private static ThreadSafeEagerSingleton instance = new ThreadSafeEagerSingleton();
    // new ThreadSafeEagerSingleton(); is done here but in simple singleton it is done in getInstance() method, 
    // so here it is thread safe because it is created at the time of class loading and in simple singleton it is created at the time of calling getInstance() method which can be called by multiple threads at the same time and hence not thread safe.
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
