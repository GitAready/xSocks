package com.github.xsocks.acceptor;

import com.github.xsocks.NamedThreadFactory;
import com.github.xsocks.core.DataForwarder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description here
 *
 * @author paddy.xie
 */
public class SocketAcceptor extends AbstractAcceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ServerBootstrap serverBootstrap;
    private EventLoopGroup acceptorGroup;
    private EventLoopGroup workerGroup;

    private String address;
    private int port;

    public SocketAcceptor() {

        acceptorGroup = new NioEventLoopGroup(2, new NamedThreadFactory("SocksAcceptor"));
        workerGroup = new NioEventLoopGroup(10, new NamedThreadFactory("SocksWorker"));

        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(acceptorGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        logger.debug("A new Socket connected " + socketChannel);
//                        socketChannel.pipeline().addFirst("", new SslHandler(null));
                        socketChannel.pipeline().addLast("XSocksRegisterHandler", new XSocksRegisterHandler());
                        socketChannel.pipeline().addLast("XSocksProcessHandler", new XSocksProcessHandler(SocketAcceptor.this));
                    }
                })
                .childOption(ChannelOption.TCP_NODELAY, true);

    }

    @Override
    public void accept() {
        serverBootstrap.bind(address, port);
    }

    @Override
    public void close() {
        acceptorGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

        DataForwarder.getInstance();
    }

    @Override
    public boolean isRemote() {
        return true;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
