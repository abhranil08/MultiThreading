/* Question and examples can be found at the last */

// Normal solution - Using Object lock
class BuildingH20 {

    private int h2O_count;

    public H2O() {
        this.h2O_count = 0; 
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        synchronized(this)
        {
            while(h2O_count==2){
            wait();
            }

            // releaseHydrogen.run() outputs "H". Do not change or remove this line.
            releaseHydrogen.run();
            h2O_count++;
            notifyAll();
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        synchronized(this){
            while(h2O_count<2){
            wait();
            }
            
            // releaseOxygen.run() outputs "O". Do not change or remove this line.
            releaseOxygen.run();
            h2O_count=0;
            notifyAll();
        }
    }
}

//Semaphore solution
/*
In the constructor:
The hydrogen semaphore is initialized with 2 permits, allowing up to two hydrogen atoms to be released before it blocks further hydrogen atoms.
The oxygen semaphore is initialized with 0 permits, initially blocking the release of oxygen atoms.

The hydrogen method:

Acquires a permit from the hydrogen semaphore. If no permits are available, the method will block until a permit becomes available.
Runs the releaseHydrogen.run() method, which outputs "H".
Checks if both hydrogen permits have been used (hydrogen.availablePermits() == 0). 
If true, it releases one permit to the oxygen semaphore, allowing the oxygen method to run.

The oxygen method:

Acquires a permit from the oxygen semaphore. If no permits are available, the method will block until a permit becomes available.
Runs the releaseOxygen.run() method, which outputs "O".
Releases two permits to the hydrogen semaphore, allowing two hydrogen atoms to be released.

Explanation of Synchronization
Initially, the hydrogen semaphore allows up to two hydrogen atoms to be released because it starts with 2 permits.
After two hydrogen atoms are released, the oxygen semaphore is given a permit, allowing one oxygen atom to be released.
Once the oxygen atom is released, the hydrogen semaphore is reset with 2 permits, allowing the process to repeat.

*/
class H2O_II {

    private final Semaphore hydrogen;
    private final Semaphore oxygen;

    public H2O() {
        hydrogen = new Semaphore(2);
        oxygen = new Semaphore(0);
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        hydrogen.acquire();
        // releaseHydrogen.run() outputs "H". Do not change or remove this line.
        releaseHydrogen.run();

        if(hydrogen.availablePermits()==0)  oxygen.release(1);
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        oxygen.acquire();
        // releaseOxygen.run() outputs "O". Do not change or remove this line.
        releaseOxygen.run();

        hydrogen.release(2);
    }
}

/*
Question - 1117. Building H2O
Medium

There are two kinds of threads: oxygen and hydrogen. Your goal is to group these threads to form water molecules.

There is a barrier where each thread has to wait until a complete molecule can be formed. Hydrogen and oxygen threads will be given releaseHydrogen and releaseOxygen methods respectively, which will allow them to pass the barrier. These threads should pass the barrier in groups of three, and they must immediately bond with each other to form a water molecule. You must guarantee that all the threads from one molecule bond before any other threads from the next molecule do.

In other words:

If an oxygen thread arrives at the barrier when no hydrogen threads are present, it must wait for two hydrogen threads.
If a hydrogen thread arrives at the barrier when no other threads are present, it must wait for an oxygen thread and another hydrogen thread.
We do not have to worry about matching the threads up explicitly; the threads do not necessarily know which other threads they are paired up with. The key is that threads pass the barriers in complete sets; thus, if we examine the sequence of threads that bind and divide them into groups of three, each group should contain one oxygen and two hydrogen threads.

Write synchronization code for oxygen and hydrogen molecules that enforces these constraints.

Example 1:

Input: water = "HOH"
Output: "HHO"
Explanation: "HOH" and "OHH" are also valid answers.
Example 2:

Input: water = "OOHHHH"
Output: "HHOHHO"
Explanation: "HOHHHO", "OHHHHO", "HHOHOH", "HOHHOH", "OHHHOH", "HHOOHH", "HOHOHH" and "OHHOHH" are also valid answers.
 

Constraints:

3 * n == water.length
1 <= n <= 20
water[i] is either 'H' or 'O'.
There will be exactly 2 * n 'H' in water.
There will be exactly n 'O' in water.
*/
