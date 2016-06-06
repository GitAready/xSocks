/**
 * Created by pxie on 5/15/16.
 */
public class TrainTest {

    public static void main(String[] args) {
        TrainTest test = new TrainTest();
        test.testRailWay();
    }

    public void testRailWay() {
        final Railway railway = new Railway(2);
        final long n = 20_000_000_000l;
        //starting a consumer thread
        new Thread() {
            long lastValue = 0;

            @Override
            public void run() {
                while (lastValue < n) {
                    Train train = railway.waitTrainOnStation(1); //waits for the train at the station #1
                    int count = train.goodsCount();
                    for (int i = 0; i < count; i++) {
                        lastValue = train.getGoods(i); // unload goods
                    }
                    railway.sendTrain(); //sends the current train to the first station.
                }
            }
        }.start();

        final long start = System.nanoTime();
        long i = 0;
        while (i < n) {
            Train train = railway.waitTrainOnStation(0); // waits for the train on the station #0
            int capacity = train.getCapacity();
            for (int j = 0; j < capacity; j++) {
                train.addGoods((int) i++); // adds goods to the train
            }
            railway.sendTrain();
            if (i % 100_000_000 == 0) { //measures the performance per each 100M items
                final long duration = System.nanoTime() - start;
                final long ops = (i * 1000L * 1000L * 1000L) / duration;
                System.out.format("ops/sec = %,d\n", ops);
                System.out.format("trains/sec = %,d\n", ops / train.getCapacity());
                System.out.format("latency nanos = %.3f%n\n",
                        duration / (float) (i) * (float) train.getCapacity());
            }
        }
    }


}
