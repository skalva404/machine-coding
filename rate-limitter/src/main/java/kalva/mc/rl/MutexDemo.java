package kalva.mc.rl;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MutexDemo implements Runnable {

    public static CyclicBarrier newBarrier
            = new CyclicBarrier(3);

    public static void main(String[] args) {
        // parent thread
        MutexDemo test = new MutexDemo();

        Thread t1 = new Thread(test);

        // start the thread
        t1.start();
    }

    public void run() {
        System.out.println(
                "Number of parties required to trip the barrier = "
                        + newBarrier.getParties());
        System.out.println(
                "Sum of product and sum = "
                        + (Computation1.product + Computation2.sum));

        // objects on which the child thread has to run
        Computation1 comp1 = new Computation1();
        Computation2 comp2 = new Computation2();

        // creation of child thread
        Thread t1 = new Thread(comp1);
        Thread t2 = new Thread(comp2);

        // moving child thread to runnable state
        t1.start();
        t2.start();

        try {
            // parent thread awaits
            MutexDemo.newBarrier.await();
        } catch (InterruptedException
                | BrokenBarrierException e) {
            e.printStackTrace();
        }

        // barrier breaks as the number of thread waiting
        // for the barrier at this point = 3
        System.out.println(
                "Sum of product and sum = "
                        + (Computation1.product + Computation2.sum));

        // Resetting the newBarrier
        newBarrier.reset();
        System.out.println("Barrier reset successful");
    }

    public static class Computation1 implements Runnable {
        public static int product = 0;

        public void run() {
            product = 2 * 3;
            try {
                // thread1 awaits for other threads
                MutexDemo.newBarrier.await();
            } catch (InterruptedException
                    | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Computation2 implements Runnable {
        public static int sum = 0;

        public void run() {
            // check if newBarrier is broken or not
            System.out.println("Is the barrier broken? - "
                    + MutexDemo.newBarrier.isBroken());
            sum = 10 + 20;
            try {
                // number of parties waiting at the barrier
                System.out.println(
                        "Number of parties waiting at the barrier "
                                + "at this point = "
                                + MutexDemo.newBarrier.getNumberWaiting());
                MutexDemo.newBarrier.await(3000,
                        TimeUnit.MILLISECONDS);

            } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
