import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by pxie on 5/13/16.
 */
public class LazySetTest {

    private int integer = 0;

    public static void main(String[] args) throws Exception {
        LazySetTest test = new LazySetTest();
        test.test();
    }

    public void test() throws Exception {
        Thread lazySetThread = new Thread() {
            @Override
            public void run() {
                integer = 1;
                int j = 1;
                for(int i = 0; i < 10000000; i++) {
                    j++;
                }
                System.out.println(j);
            }
        };

        Thread checkThread = new Thread() {
            @Override
            public void run() {
                int i = integer;
                System.out.println(i);
            }
        };

        lazySetThread.start();
        checkThread.start();

        lazySetThread.join();
        checkThread.join();
    }

}
