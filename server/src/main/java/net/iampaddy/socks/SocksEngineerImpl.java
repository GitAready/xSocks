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
public class SocksEngineerImpl implements SocksEngineer {

    private Logger logger = LoggerFactory.getLogger(SocksEngineerImpl.class);

    private ExecutorService acceptorPool = null;
    private Lock statusLock = null;
    private volatile EngineerStatus status;

    private AsynchronousChannelGroup group;
    private AsynchronousServerSocketChannel serverSocketChannel;

    private SocksEngineerImpl() {
        this.statusLock = new ReentrantLock();
        this.status = EngineerStatus.SHUTDOWN;
    }

    public static SocksEngineerImpl createNewEngineer() {
        return new SocksEngineerImpl();
    }

    public void startup(Context context) {
        statusLock.lock();
        switchStatus();
        try {
            logger.info("Starting xSocks server...");

            logger.info("Initialize socks acceptor thread pool...");
            acceptorPool = new ThreadPoolExecutor(1, 20, 50, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(),
                    new NamedThreadFactory("SocksAcceptor"));

//            logger.info("Initialize socks worker thread pool...");
//            ExecutorService workerPool = new ThreadPoolExecutor(1, 1, 50, TimeUnit.SECONDS,
//                    new SynchronousQueue<Runnable>(),
//                    new NamedThreadFactory("SocksWorker"));

            try {
                group = AsynchronousChannelGroup.withThreadPool(acceptorPool);
                serverSocketChannel = AsynchronousServerSocketChannel.open(group);
                serverSocketChannel.bind(new InetSocketAddress("localhost", 1080), 100);
                logger.info("Listening on port 8080");
                serverSocketChannel.accept(new Context(), new SocketChannelAcceptHandler(this, serverSocketChannel));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            logger.info("xSocks server started");
        } finally {
            switchStatus();
            statusLock.unlock();
        }
    }

    public void shutdown() {
        statusLock.lock();
        switchStatus();
        try {
            // 1. stop accept new connection
            // 2. determine the internal work
            // 3.
        } finally {
            switchStatus();
            statusLock.unlock();
        }
    }

    @Override
    public void submit(AsynchronousSocksWork socksWork) {
        isRunning();
//        service.submit(socksWork);

    }

    @Override
    public EngineerStatus status() {
        statusLock.lock();
        try {
            return status;
        } finally {
            statusLock.unlock();
        }
    }

    public boolean isRunning() {
        return status() == EngineerStatus.RUNNING;
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
