package net.iampaddy.socks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

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
        Thread t = new Thread(group, r, threadName + "-" + seq.getAndIncrement()) {
            @Override
            public void run() {
                logger.debug(this.getName() + " started");
                super.run();
                logger.debug(this.getName() + " stopped");

                if(this.getThreadGroup().activeCount() == 0) {
                    logger.info(this.getThreadGroup().getName() + " shutdown completed");
                }
            }
        };

        if (t.isDaemon()) {
            t.setDaemon(false);
        }

        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }


        return t;
    }

}
