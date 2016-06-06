import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.System.out;


public final class TestLocks implements Runnable {


    public enum LockType {JVM, JUC}


    public static LockType lockType;


    public static final long WARMUP_ITERATIONS = 100L * 1000L;

    public static final long ITERATIONS = 500L * 1000L * 1000L;

    public static long counter = 0L;


    public static final Object jvmLock = new Object();

    public static final Lock jucLock = new ReentrantLock();

    private static int numThreads;


    private final long iterationLimit;

    private final CyclicBarrier barrier;


    public TestLocks(final CyclicBarrier barrier, final long iterationLimit) {
        this.barrier = barrier;
        this.iterationLimit = iterationLimit;
    }


    public static void main(final String[] args) throws Exception {

        lockType = LockType.valueOf(args[0]);
        numThreads = Integer.parseInt(args[1]);


        for (int i = 0; i < 10; i++) {
            runTest(numThreads, WARMUP_ITERATIONS);
            counter = 0L;
        }

        final long start = System.nanoTime();
        runTest(numThreads, ITERATIONS);
        final long duration = System.nanoTime() - start;

        out.printf("%d threads, duration %,d (ns)\n", numThreads, duration);
        out.printf("%,d ns/op\n", duration / ITERATIONS);
        out.printf("%,d ops/s\n", (ITERATIONS * 1000000000L) / duration);
        out.println("counter = " + counter);
    }


    private static void runTest(final int numThreads, final long iterationLimit)
            throws Exception {
        CyclicBarrier barrier = new CyclicBarrier(numThreads);
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new TestLocks(barrier, iterationLimit));
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }
    }


    public void run() {
        try {
            barrier.await();
        } catch (Exception e) {
            // don't care
        }

        switch (lockType) {
            case JVM:
                jvmLockInc();
                break;
            case JUC:
                jucLockInc();
                break;
        }
    }


    private void jvmLockInc() {
        long count = iterationLimit / numThreads;
        while (0 != count--) {
            synchronized (jvmLock) {
                ++counter;
            }
        }
    }


    private void jucLockInc() {
        long count = iterationLimit / numThreads;
        while (0 != count--) {
            jucLock.lock();
            try {
                ++counter;
            } finally {
                jucLock.unlock();
            }
        }
    }
}