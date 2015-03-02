package net.iampaddy.socks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Description
 *
 * @author paddy.xie
 */
public class SocketChannelHandler implements CompletionHandler<AsynchronousSocketChannel, Context> {

    private Logger logger = LoggerFactory.getLogger(SocketChannelHandler.class);

    private AsynchronousServerSocketChannel serverSocketChannel;
    private SocksEngineer socksEngineer;

    public SocketChannelHandler(AsynchronousSocksEngineer socksEngineer,
                                AsynchronousServerSocketChannel serverSocketChannel) {
        this.serverSocketChannel = serverSocketChannel;
        this.socksEngineer = socksEngineer;
    }

    @Override
    public void completed(AsynchronousSocketChannel socketChannel, Context context) {

        // ready for next connection
        serverSocketChannel.accept(context, this);

        logger.info("Accept a connection : " + socketChannel);
        socksEngineer.submit(new AsynchronousSocksWork<>(socketChannel, new Context()));

    }

    @Override
    public void failed(Throwable exc, Context attachment) {
        logger.error(exc.getMessage(), exc);
    }
}
