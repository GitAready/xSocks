import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by pxie on 5/14/16.
 */
public class OneToOneQueueTest {

    public static final long TOTAL = 100_1000_1000L;

    public static void main(String[] args) throws InterruptedException {

        OneToOneQueueTest test = new OneToOneQueueTest(1024 * 64, 1, 1, 1);
        test.start();

    }


    private OneToOneQueue<Long[]> oneToOneQueue;
    private Thread[] producers, consumers;

    private int arraySize;
    private CyclicBarrier barrier;


    private AtomicLong time = new AtomicLong(0);

    public OneToOneQueueTest(int capacity, int arraySize, int producerCount, int consumerCount) {
        oneToOneQueue = createQueue(capacity);

        this.arraySize = arraySize;
        this.barrier = new CyclicBarrier(producerCount + consumerCount);

        producers = new Thread[producerCount];
        for (int i = 0; i < producerCount; i++) {
            producers[i] = createProducer();
        }

        consumers = new Thread[consumerCount];
        for (int i = 0; i < consumerCount; i++) {
            consumers[i] = createConsumer();
        }
    }

    public void start() throws InterruptedException {

        for (Thread producer : producers) {
            producer.start();
        }

        for (Thread consumer : consumers) {
            consumer.start();
        }

        for (Thread producer : producers) {
            producer.join();
        }

        for (Thread consumer : consumers) {
            consumer.join();
        }


    }

    private OneToOneQueue<Long[]> createQueue(int capacity) {
        return new OneToOneQueue<>(Long[].class, capacity);
    }

    private Thread createProducer() {
        return new Thread() {
            @Override
            public void run() {

                Long[] array = new Long[arraySize];
                try {
                    barrier.await();
                } catch (Exception e) {
                    // ignore
                }

                long start = System.nanoTime();
                long value = 0;
                while (value < TOTAL) {
                    for (int i = 0; i < arraySize && value < TOTAL; i++) {
                        array[i] = value++;
                    }
                    oneToOneQueue.put(array);
                }
                long result = System.nanoTime() - start;
                time.addAndGet(result);

                System.out.format("%,d ops/sec", TOTAL / (time.get() / 1000L / 1000L / 1000L));
            }
        };
    }

    private Thread createConsumer() {
        return new Thread() {
            @Override
            public void run() {
                try {
                    barrier.await();
                } catch (Exception e) {
                    // ignore
                }

                Long[] array;
                long value = 0;
                while (value != TOTAL - 1) {
                    array = oneToOneQueue.get();
                    for (Long anArray : array) {
                        value = anArray;
                        if (value == (TOTAL - 1)) {
                            break;
                        }
                    }
                }
            }
        };
    }

}


