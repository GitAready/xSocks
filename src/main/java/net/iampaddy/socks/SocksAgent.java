package net.iampaddy.socks;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.iampaddy.socks.connector.Connector;
import net.iampaddy.socks.dns.DNSResolver;
import net.iampaddy.socks.handler.ProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Description
 *
 * @author paddy.xie
 */
public class SocksAgent {

    private Logger logger = LoggerFactory.getLogger(SocksAgent.class);

    private Lock statusLock;
    private volatile Status status;

    private ServerBootstrap serverBootstrap;

    private Configuration conf;

    private SocksAgent(Configuration conf) {
        this.statusLock = new ReentrantLock();
        this.status = Status.SHUTDOWN;

        this.conf = conf;
    }

    public static void main(String[] args) {
        Properties prop = new Properties();

        try (InputStream is = SocksAgent.class.getResourceAsStream("/xsocks.conf")) {
            prop.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Loading conf file failed...");
        }

        Configuration conf = new Configuration(prop);
        final SocksAgent agent = new SocksAgent(conf);
        agent.startup();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                agent.shutdown();
            }
        });
    }

    public void startup() {
        List<Connector> conns = conf.getConnector();
        DNSResolver resolver = conf.getDNSResolver();

        statusLock.lock();
        try {
            logger.info("Starting xSocks server...");
            switchStatus();

            EventLoopGroup acceptorGroup = new NioEventLoopGroup(conf.getInteger("agent.acceptor"), new NamedThreadFactory("SocksAcceptor"));
            EventLoopGroup workerGroup = new NioEventLoopGroup(conf.getInteger("agent.worker"), new NamedThreadFactory("SocksWorker"));

            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(acceptorGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            if (logger.isDebugEnabled()) {
                                logger.debug("A new Socket connected " + socketChannel);
                            }
                            socketChannel.pipeline().addLast(ProtocolHandler.class.getName(), new ProtocolHandler());
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
            serverBootstrap.group().shutdownGracefully();
            // 2. determine the internal work
            // 3.
        } finally {
            switchStatus();
            statusLock.unlock();
        }
    }

    public Status status() {
        statusLock.lock();
        try {
            return status;
        } finally {
            statusLock.unlock();
        }
    }

    public boolean isRunning() {
        return status() == Status.RUNNING;
    }

    private void switchStatus() {
        switch (status) {
            case SHUTDOWN:
                status = Status.STARTING;
                break;
            case STARTING:
                status = Status.RUNNING;
                break;
            case RUNNING:
                status = Status.SHUTTING_DOWN;
                break;
            case SHUTTING_DOWN:
                status = Status.SHUTDOWN;
                break;
        }
        logger.info("Server switched to " + status);
    }

    private enum Status {

        SHUTDOWN, STARTING, RUNNING, SHUTTING_DOWN


    }

}
