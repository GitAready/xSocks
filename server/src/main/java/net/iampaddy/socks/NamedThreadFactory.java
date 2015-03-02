package net.iampaddy.socks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description
 *
 * @author paddy.xie
 */
public class NamedThreadFactory implements ThreadFactory {

    private Logger logger = LoggerFactory.getLogger(NamedThreadFactory.class);

    private AtomicInteger seq;

    private final String threadName;
    private ThreadGroup group;

    public NamedThreadFactory(String threadGroupName) {
        this.seq = new AtomicInteger(1);

        this.threadName = threadGroupName;
        this.group = new ThreadGroup(threadGroupName);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, threadName + " - " + seq.getAndIncrement()) {
            @Override
            public void run() {
                try {
                    super.run();
                } catch(Throwable t) {
                    logger.error(t.getMessage(), t);
                }
            }
        };

        if(t.isDaemon()) {
            t.setDaemon(false);
        }

        if(t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }


        return t;
    }

}
