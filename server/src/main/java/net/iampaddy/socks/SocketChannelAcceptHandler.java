package net.iampaddy.socks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Description
 *
 * @author paddy.xie
 */
public class SocketChannelAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Context> {

    private Logger logger = LoggerFactory.getLogger(SocketChannelAcceptHandler.class);

    private AsynchronousServerSocketChannel serverSocketChannel;
    private SocksEngine socksEngine;

    public SocketChannelAcceptHandler(SocksEngineImpl socksEngineer,
                                      AsynchronousServerSocketChannel serverSocketChannel) {
        this.serverSocketChannel = serverSocketChannel;
        this.socksEngine = socksEngineer;
    }

    public void completed(AsynchronousSocketChannel socketChannel, Context context) {

        logger.info("Accept a connection : " + socketChannel);

        // ready for next connection
        if(socksEngine.isRunning()) {
            serverSocketChannel.accept(context, this);
        }

        ByteBuffer buffer = ByteBuffer.allocate(100);
        socketChannel.read(buffer, buffer, new Socks5Handler(socketChannel));

    }

    public void failed(Throwable exc, Context attachment) {
        logger.error(exc.getMessage(), exc);
    }
}
