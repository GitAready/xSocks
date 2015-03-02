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

//    private ExecutorService service = null;
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

    public void startup(Context context) {
        lock.lock();
        switchStatus();
        try {
            logger.info("Starting xSocks server...");

            logger.debug("Initialize socks worker pool");
//            service = new ThreadPoolExecutor(1, 1, 50, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
//                    new NamedThreadFactory("SocksWorker"));

            try {
                ExecutorService aioThreadPool = new ThreadPoolExecutor(1, 1, 50, TimeUnit.SECONDS,
                        new SynchronousQueue<Runnable>(),
                        new NamedThreadFactory("SocksAcceptor"));
                group = AsynchronousChannelGroup.withThreadPool(aioThreadPool);
                serverSocketChannel = AsynchronousServerSocketChannel.open(group);
                serverSocketChannel.bind(new InetSocketAddress("localhost", 1080), 100);
                logger.info("Listening on port 8080");

                serverSocketChannel.accept(new Context(), new SocketChannelHandler(this, serverSocketChannel));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            logger.info("xSocks server started");
        } finally {
            switchStatus();
            lock.unlock();
        }
    }

    public void shutdown() {
        lock.lock();
        switchStatus();
        try {
            try {
                logger.info("Stop accepting new connection");
                serverSocketChannel.close();
            } catch (IOException e) {
                logger.error("Close server socket channel failed");
            }
//            service.shutdown();
        } finally {
            switchStatus();
            lock.unlock();
        }
    }

    @Override
    public void submit(AsynchronousSocksWork socksWork) {
        isRunning();
//        service.submit(socksWork);

    }

    @Override
    public EngineerStatus status() {
        return status;
    }

    private boolean isRunning() {
        return status == EngineerStatus.RUNNING;
    }

    private void switchStatus() {
        switch (status) {
            case SHUTDOWN:
                status = EngineerStatus.STARTING;
                break;
            case STARTING:
                status = EngineerStatus.RUNNING;
                break;
            case RUNNING:
                status = EngineerStatus.SHUTING_DOWN;
                break;
            case SHUTING_DOWN:
                status = EngineerStatus.SHUTDOWN;
                break;
        }
        logger.info("Server switched to " + status);
    }
}
