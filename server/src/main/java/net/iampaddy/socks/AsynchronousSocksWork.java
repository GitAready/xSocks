package net.iampaddy.socks;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Description
 *
 * @author paddy.xie
 */
public class AsynchronousSocksWork<S extends AsynchronousSocketChannel, A> implements Runnable {

    private AsynchronousSocketChannel socketChannel;
    private A attachment;

    public AsynchronousSocksWork(S socketChannel, A attachment) {
        this.socketChannel = socketChannel;
        this.attachment = attachment;
    }

    @Override
    public void run() {
        final ByteBuffer buffer = ByteBuffer.allocate(1024);

        socketChannel.read(buffer, null, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {

            }

            @Override
            public void failed(Throwable exc, Object attachment) {

            }
        });
    }
}
