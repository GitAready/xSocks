package com.github.xsocks.connector;

import com.github.xsocks.NamedThreadFactory;
import com.github.xsocks.socket.DestKey;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * Description here
 *
 * @author paddy.xie
 */
public class SocketConnector implements Connector {

    private EventLoopGroup workerGroup;

    private int worker;

    private String serverAddress;
    private int port;

    @Override
    public void connect() {

        workerGroup = new NioEventLoopGroup(worker, new NamedThreadFactory("DataForwarder"));

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)




    }

    @Override
    public void disconnect() {

    }

    @Override
    public void write(ByteBuf buffer) {

    }

    @Override
    public boolean resolve(DestKey destKey) {
        return false;
    }

}
