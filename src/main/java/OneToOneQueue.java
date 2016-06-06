import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by pxie on 5/15/16.
 */
public class OneToOneQueue<T> {

    private final int capacity;

    private long p0, p1, p2, p3, p4, p5, p6;
    private volatile int head;
    private int p8;
    private long p9, p10, p11, p12, p13, p14, p15;
    private AtomicInteger tail;

    private T[] buffer;

    @SuppressWarnings("unchecked")
    public OneToOneQueue(Class<T> clazz, int capacity) {
        this.capacity = capacity;
        this.buffer = (T[])Array.newInstance(clazz, capacity);

        this.head = 0;
        this.tail = new AtomicInteger(0);
    }

    public T get() {
        int t = tail.get();
        while (t >= head) {
            Thread.yield();
        }
        int index = t % capacity;
        tail.lazySet(t + 1);
        return buffer[index];
    }

    public void put(T value) {
        int h = head;

        while(h - tail.get() >= capacity) {
            Thread.yield();
        }

        int index = h % capacity;
        buffer[index] = value;
        head = h + 1;
    }
}