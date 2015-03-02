package net.iampaddy.socks;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

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
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

        socketChannel.read(buffer);
        buffer.flip();
        System.out.println(buffer);
    }
}
