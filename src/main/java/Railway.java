import java.util.concurrent.atomic.AtomicInteger;

public class Railway {
    private final Train train = new Train(2 * 1024);
    //站台号码stationNo 跟踪火车，定义哪个站点接受火车
    private final AtomicInteger stationIndex = new AtomicInteger();

    private int stationCount;

    public Railway(int stationCount) {
        this.stationCount = stationCount;
    }

    //多线程访问这个方法，也就是在特定站点等待火车
    public Train waitTrainOnStation(final int stationNo) {

        while (stationIndex.get() % stationCount != stationNo) {
            Thread.yield(); // this is necessary to keep a high throughput of message passing.
            //But it eats CPU cycles while waiting for a train
        }
        // the busy loop returns only when the station number will match
        // stationIndex.get() % stationCount condition

        return train;
    }

    //这个方法通过增加火车站台号将火车移到下一个站点。
    public void sendTrain() {
        stationIndex.getAndIncrement();
    }
}