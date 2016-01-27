package net.iampaddy.socks.acceptor;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.iampaddy.socks.NamedThreadFactory;
import net.iampaddy.socks.handler.ProtocolHandler;
import org.bouncycastle.crypto.tls.TlsProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description here
 *
 * @author paddy.xie
 */
public class SocketAcceptor implements Acceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ServerBootstrap serverBootstrap;

    public SocketAcceptor() {

        EventLoopGroup acceptorGroup = new NioEventLoopGroup(2, new NamedThreadFactory("SocksAcceptor"));
        EventLoopGroup workerGroup = new NioEventLoopGroup(10, new NamedThreadFactory("SocksWorker"));

        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(acceptorGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        logger.debug("A new Socket connected " + socketChannel);
                        socketChannel.pipeline().addFirst("", new SslHandler());
                        socketChannel.pipeline()
                                .addLast(ProtocolHandler.class.getName(), new ProtocolHandler());
                    }
                })
                .childOption(ChannelOption.TCP_NODELAY, true);

    }

    @Override
    public void accept() {
        serverBootstrap.bind("localhost", 1080);
    }

    @Override
    public void close() {

    }

    @Override
    public void closeImmediately() {

    }

}
