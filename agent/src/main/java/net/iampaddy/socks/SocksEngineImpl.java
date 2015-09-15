package net.iampaddy.socks;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.iampaddy.socks.codec.Socks5AuthCodec;
import net.iampaddy.socks.handler.FlushHandler;
import net.iampaddy.socks.handler.ProtocolHandler;
import net.iampaddy.socks.handler.Socks5AuthHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static net.iampaddy.socks.EngineerStatus.*;

/**
 * Description
 *
 * @author paddy.xie
 */
public class SocksEngineImpl implements SocksEngine {

    private Logger logger = LoggerFactory.getLogger(SocksEngineImpl.class);

    private ExecutorService acceptorPool = null;
    private Lock statusLock = null;
    private volatile EngineerStatus status;

    private AsynchronousChannelGroup group;
    private AsynchronousServerSocketChannel serverSocketChannel;

    private SocksEngineImpl() {
        this.statusLock = new ReentrantLock();
        this.status = SHUTDOWN;
    }

    public static SocksEngineImpl createNewEngineer() {
        return new SocksEngineImpl();
    }

    public void startup(Context context) {
        statusLock.lock();
        switchStatus();
        try {
            logger.info("Starting xSocks server...");

            EventLoopGroup acceptorGroup = new NioEventLoopGroup(5, new NamedThreadFactory("SocksAcceptor"));
            EventLoopGroup workerGroup = new NioEventLoopGroup(5, new NamedThreadFactory("SocksWorker"));

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(acceptorGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            logger.debug("A new Socket connected " + socketChannel);
                             socketChannel.pipeline()
                                    .addLast(FlushHandler.class.getName(), new FlushHandler())
                                    .addLast(ProtocolHandler.class.getName(), new ProtocolHandler());
                        }
                    })
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .bind("localhost", 1080);

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

    public void submit(AsynchronousSocksWork socksWork) {
        isRunning();
//        service.submit(socksWork);

    }

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
                status = SHUTDOWN;
                break;
        }
        logger.info("Server switched to " + status);
    }
}
