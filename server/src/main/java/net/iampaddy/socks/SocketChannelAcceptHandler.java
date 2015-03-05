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
    private SocksEngineer socksEngineer;

    public SocketChannelAcceptHandler(SocksEngineerImpl socksEngineer,
                                      AsynchronousServerSocketChannel serverSocketChannel) {
        this.serverSocketChannel = serverSocketChannel;
        this.socksEngineer = socksEngineer;
    }

    @Override
    public void completed(AsynchronousSocketChannel socketChannel, Context context) {

        logger.info("Accept a connection : " + socketChannel);

        // ready for next connection
        if(socksEngineer.isRunning()) {
//            serverSocketChannel.accept(context, this);
        }

        ByteBuffer buffer = ByteBuffer.allocate(100);
        socketChannel.read(buffer, buffer, new ProtocolDetectHandler(socketChannel));

    }

    @Override
    public void failed(Throwable exc, Context attachment) {
        logger.error(exc.getMessage(), exc);
    }
}
