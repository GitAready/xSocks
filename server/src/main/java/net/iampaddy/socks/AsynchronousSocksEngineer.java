package net.iampaddy.socks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description
 *
 * @author paddy.xie
 */
public class AsynchronousSocksEngineer implements SocksEngineer {

    private Logger logger = LoggerFactory.getLogger(AsynchronousSocksEngineer.class);

    private ExecutorService service = null;
    private Lock lock = null;
    private volatile EngineerStatus status;

    private AsynchronousChannelGroup group;
    private AsynchronousServerSocketChannel serverSocketChannel;

    private AsynchronousSocksEngineer() {
        this.lock = new ReentrantLock();
        this.status = EngineerStatus.SHUTDOWN;
    }

    public static AsynchronousSocksEngineer createNewEngineer() {
        return new AsynchronousSocksEngineer();
    }

    public void startup() {
        lock.lock();
        try {
            logger.info("Starting xSocks server...");

            service = new ThreadPoolExecutor(5, 50, 50, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
                    new NamedThreadFactory("SocksWorker"));

            service.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        group = AsynchronousChannelGroup.withThreadPool(
                                Executors.newSingleThreadExecutor(new NamedThreadFactory("SocksAcceptor")));
                        serverSocketChannel = AsynchronousServerSocketChannel.open(group);
                        serverSocketChannel.bind(new InetSocketAddress("localhost", 1080), 100);
                        serverSocketChannel.accept(new Context(), new AsynchronousSocketChannelHandler(AsynchronousSocksEngineer.this, serverSocketChannel));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            logger.info("");
        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        lock.lock();
        try {
//            serverSocketChannel.
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void submit(AsynchronousSocksWork socksWork) {
        isRunning();
        service.submit(socksWork);

    }

    @Override
    public EngineerStatus status() {
        return status;
    }

    private boolean isRunning() {
        return status == EngineerStatus.RUNNING;
    }
}
