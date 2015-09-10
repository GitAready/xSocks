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
public class Socks5Handler implements CompletionHandler<Integer, ByteBuffer> {

    private Logger logger = LoggerFactory.getLogger(Socks5Handler.class);

    private AsynchronousSocketChannel socketChannel;

    public Socks5Handler(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void completed(Integer result, ByteBuffer buffer) {
        logger.info(socketChannel + " : " + result);
        if(result <= 0) {
            logger.info(socketChannel + " : " + result);
            return;
        }
        buffer.flip();
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
            future.get();
            buffer.clear();
            future = socketChannel.read(buffer);
            future.get();
            buffer.flip();
            byte[] array = buffer.array();
            final int length = array[4];
            final byte[] address = new byte[length];
            System.arraycopy(array, 5, address, 0, length);
            String domainName = new String(address);
            int port = 80;

            try {
                logger.info("domain: {}", domainName);
                if(!domainName.equals("www.baidu.com")) {
                    socketChannel.close();
                    return;
                }
                buffer.clear();
                buffer.put(new byte[]{0x05, 0x00, 0x00, 0x01, (byte)192, (byte)168, (byte)16, (byte)1});
                buffer.put(new byte[]{0x00, (byte) 0x80});
                buffer.flip();
                future = socketChannel.write(buffer);
                future.get();
//                AsynchronousSocketChannel remoteSocketChannel = AsynchronousSocketChannel.open();
//                remoteSocketChannel.connect(new InetSocketAddress(domainName, port), remoteSocketChannel,
//                        new CompletionHandler<Void, AsynchronousSocketChannel>() {
//                            @Override
//                            public void completed(Void result, AsynchronousSocketChannel remoteSocketChannel) {
//                                ByteBuffer buffer = ByteBuffer.allocate(10);
//                                buffer.put(new byte[]{0x05, 0x00, 0x00, 0x01});
//                                try {
//                                    buffer.put(((InetSocketAddress)remoteSocketChannel.getRemoteAddress()).getAddress().getAddress());
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                buffer.put(new byte[]{0x00, (byte) 0x80});
//                                buffer.flip();
//                                Future future = socketChannel.write(buffer);
//                                try {
//                                    future.get();
//
//                                    final ByteBuffer clientBuffer = ByteBuffer.allocate(100);
//                                    socketChannel.read(clientBuffer, remoteSocketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
//                                        @Override
//                                        public void completed(Integer result, AsynchronousSocketChannel remoteSocketChannel) {
//                                            if(result <=0 ) {
//                                                System.out.println("received data : " + result);
//                                                return;
//                                            }
//                                            clientBuffer.flip();
//                                        }
//
//                                        @Override
//                                        public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
//                                            logger.error(exc.getMessage(), exc);
//                                        }
//                                    });
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
//
//                            }
//                        });

            } catch (IOException e) {
                e.printStackTrace();
            }

            buffer.clear();

            new Thread() {

                @Override
                public void run() {

                    ByteBuffer  buffer = ByteBuffer.allocate(1024);
                    socketChannel.read(buffer);

                }
            }.start();



        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void failed(Throwable exc, ByteBuffer attachment) {
        logger.error(exc.getMessage(), exc);
    }
}
