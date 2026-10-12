/*
Thread-safe Lazy initialization
Terminology: Synchronized Lazy Initialization Singleton.

This is also called the synchronized block approach.How does it work?

The important part is:

synchronized (ThreadSafeLockingSingleton.class)

It means only one thread can execute the synchronized block for this class at a time.

Imagine two threads:
Thread 1                  Thread 2
   |                         |
   | getInstance()            | getInstance()
   |                         |
   | Acquires lock            | Waits for lock
   |                         |
   | instance == null         |
   |                         |
   | Creates Object A         |
   |                         |
   | Releases lock            |
   |                         | Acquires lock
   |                         |
   |                         | instance != null
   |                         | Returns Object A

Now only one instance gets created. What's the disadvantage?

Every time you call: getInstance()
you acquire the lock, even when the instance already exists.

For example:
First call  → Lock → Create object → Unlock
Second call → Lock → Return object → Unlock
Third call  → Lock → Return object → Unlock
Fourth call → Lock → Return object → Unlock

The locking overhead is unnecessary after the object has been created.

That's what Double-Checked Locking tries to improve.
*/

public class ThreadSafeLockingSingleton {
    private static ThreadSafeLockingSingleton instance = null;

    private ThreadSafeLockingSingleton() {
        System.out.println("Singleton Constructor Called!");
    }

    public static ThreadSafeLockingSingleton getInstance() {
        synchronized (ThreadSafeLockingSingleton.class) { // Lock for thread safety
            if (instance == null) {
                instance = new ThreadSafeLockingSingleton();
            }
            return instance;
        }
    }

    public static void main(String[] args) {
        ThreadSafeLockingSingleton s1 = ThreadSafeLockingSingleton.getInstance();
        ThreadSafeLockingSingleton s2 = ThreadSafeLockingSingleton.getInstance();

        System.out.println(s1 == s2);
    }
}
