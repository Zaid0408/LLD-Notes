/*
Thread-safe when corrected Lazy initialization
The idea is to check whether the instance exists twice.Why twice?

First check — Before acquiring the lock

if (instance == null)

If the instance already exists, return it immediately without acquiring a lock.

Second check — After acquiring the lock
Why check again?
Imagine two threads both pass the first check.

Thread 1                  Thread 2
   |                         |
   | instance == null        | instance == null
   |                         |
   | Acquires lock           | Waits for lock
   |                         |
   | Creates Object A        |
   | Releases lock           |
   |                         | Acquires lock
   |                         |
   |                         | instance != null
   |                         | Does not create object

The second check prevents Thread 2 from creating another instance.
Important correction in your implementation!
Your implementation is missing one keyword: volatile.
volatile prevents this unsafe publication by guaranteeing visibility and the necessary ordering between threads.
*/

public class ThreadSafeDoubleLockingSingleton {
    private static volatile ThreadSafeDoubleLockingSingleton instance = null;

    private ThreadSafeDoubleLockingSingleton() {
        System.out.println("Singleton Constructor Called!");
    }

    // Double check locking..
    public static ThreadSafeDoubleLockingSingleton getInstance() {
        if (instance == null) { // First check (no locking)
            synchronized (ThreadSafeDoubleLockingSingleton.class) { // Lock only if needed
                if (instance == null) { // Second check (after acquiring lock)
                    instance = new ThreadSafeDoubleLockingSingleton();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        ThreadSafeDoubleLockingSingleton s1 = ThreadSafeDoubleLockingSingleton.getInstance();
        ThreadSafeDoubleLockingSingleton s2 = ThreadSafeDoubleLockingSingleton.getInstance();

        System.out.println(s1 == s2);
    }
}
