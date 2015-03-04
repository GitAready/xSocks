package net.iampaddy.socks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Description
 *
 * @author paddy.xie
 */
public class ProtocolDetectHandler implements CompletionHandler<Integer, ByteBuffer> {

    private Logger logger = LoggerFactory.getLogger(ProtocolDetectHandler.class);

    private AsynchronousSocketChannel socketChannel;

    public ProtocolDetectHandler(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        buffer.flip();
        if(!buffer.hasRemaining()) {
            return;
        }
        byte version = buffer.get();
        byte methodNumber = buffer.get();
        byte[] methods = new byte[methodNumber];
        buffer.get(methods);

        String methodsString = "" + methods[0];
        logger.info("Version: {}", version);
        for(int i = 1; i < methods.length; i++) {
            methodsString += "," + methods[i];
        }
        logger.info("Supported Method: {}", methodsString);

        buffer.clear();
        buffer.put(new byte[]{(byte) 0x05, (byte) 0x00});
        buffer.flip();
        Future<Integer> future = socketChannel.write(buffer);
        try {
            int r = future.get();
            buffer.clear();
            future = socketChannel.read(buffer);
            future.get();
            byte[] domainName = new byte[13];
            buffer.flip();
            buffer.position(5);
            buffer.get(domainName);
            System.out.println(new String(domainName));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        logger.error(exc.getMessage(), exc);
    }
}
