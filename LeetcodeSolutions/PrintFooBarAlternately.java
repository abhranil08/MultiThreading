// Simple wait and notify solution
class PrintFooBarAlternately {
    private int n;
    private boolean printFooFlag;

    public FooBar(int n) {
        this.n = n;
        this.printFooFlag = true;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        
        for (int i = 0; i < n; i++) {
            synchronized(this)
            {
                while(!printFooFlag){
                    wait();
                }
            
        	      // printFoo.run() outputs "foo". Do not change or remove this line.
        	      printFoo.run();
                printFooFlag = false;
                notifyAll();
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        
        for (int i = 0; i < n; i++) {
            synchronized(this)
            {
                while(printFooFlag){
                    wait();
                }
            
                // printBar.run() outputs "bar". Do not change or remove this line.
        	      printBar.run();

                printFooFlag=true;
                notifyAll();
            }
        }
    }
}

//Semaphore solution
/*
The FooBar class contains:

An integer n which denotes the number of times foo and bar should be printed.
Two semaphores:
foo initialized with 1 permit, allowing the foo method to run first.
bar initialized with 0 permits, blocking the bar method until a permit is released by foo.

Explanation of Synchronization :

Initially, the foo semaphore has 1 permit, and the bar semaphore has 0 permits. This allows the foo method to execute first.
After foo prints "foo", it releases a permit to the bar semaphore, allowing the bar method to run.
The bar method prints "bar" and then releases a permit to the foo semaphore, allowing the foo method to run again.
This alternating pattern continues for n iterations, ensuring that "foo" and "bar" are printed in an alternating sequence.
*/
class FooBar {
    private final int n;
    private final Semaphore foo = new Semaphore(1);
    private final Semaphore bar = new Semaphore(0);

    public FooBar(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            foo.acquire();
        	printFoo.run();
            bar.release();
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            bar.acquire();
        	printBar.run();
            foo.release();
        }
    }
}

/*
Question - Suppose you are given the following code:

class FooBar {
  public void foo() {
    for (int i = 0; i < n; i++) {
      print("foo");
    }
  }

  public void bar() {
    for (int i = 0; i < n; i++) {
      print("bar");
    }
  }
}
The same instance of FooBar will be passed to two different threads:

thread A will call foo(), while
thread B will call bar().
Modify the given program to output "foobar" n times.

 

Example 1:

Input: n = 1
Output: "foobar"
Explanation: There are two threads being fired asynchronously. One of them calls foo(), while the other calls bar().
"foobar" is being output 1 time.
Example 2:

Input: n = 2
Output: "foobarfoobar"
Explanation: "foobar" is being output 2 times.
 

Constraints:
1 <= n <= 1000
*/
