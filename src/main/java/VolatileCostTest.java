public class VolatileCostTest {

    private int i;
    private volatile int j;

    public static void main(String[] args) {
        VolatileCostTest test = new VolatileCostTest();
        test.test();
    }

    public void test() {

        int k = i = 1;
        long start = System.nanoTime();
        for(long count = 0; count < 100_1000_1000L; count++) {
            k = i;
            k++;
        }
        System.out.println(k);
        long result = System.nanoTime() - start;
        System.out.println(result/1000/1000);

        k = j = 1;
        start = System.nanoTime();
        for(long count = 0; count < 100_1000_1000L; count++) {
            k = j;
            k++;
        }
        System.out.println(k);
        result = System.nanoTime() - start;
        System.out.println(result/1000/1000);

        k = i = 1;
        start = System.nanoTime();
        for(long count = 0; count < 100_1000_1000L; count++) {
            k = i;
            k++;
        }
        System.out.println(k);
        result = System.nanoTime() - start;
        System.out.println(result/1000/1000);

        k = j = 1;
        start = System.nanoTime();
        for(long count = 0; count < 100_1000_1000L; count++) {
            k = j;
            k++;
        }
        System.out.println(k);
        result = System.nanoTime() - start;
        System.out.println(result/1000/1000);

    }
}
