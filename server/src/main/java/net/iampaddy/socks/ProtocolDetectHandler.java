package net.iampaddy.socks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
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
        if(result <= 0) {
            logger.info("No data, don't know why...");
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
                AsynchronousSocketChannel remoteSocketChannel = AsynchronousSocketChannel.open();
                remoteSocketChannel.connect(new InetSocketAddress(domainName, port), remoteSocketChannel,
                        new CompletionHandler<Void, AsynchronousSocketChannel>() {
                            @Override
                            public void completed(Void result, AsynchronousSocketChannel remoteSocketChannel) {
                                ByteBuffer buffer = ByteBuffer.allocate(1024);
                                buffer.put(new byte[]{0x05, 0x00, 0x00, 0x03, (byte) length});
                                buffer.put(address);
                                buffer.put(new byte[]{0x00, (byte) 0x80});
                                buffer.flip();
                                Future future = socketChannel.write(buffer);
                                try {
                                    future.get();

                                    final ByteBuffer clientBuffer = ByteBuffer.allocate(100);
                                    socketChannel.read(clientBuffer, remoteSocketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
                                        @Override
                                        public void completed(Integer result, AsynchronousSocketChannel remoteSocketChannel) {
                                            if(result <=0 ) return;
                                            clientBuffer.flip();
                                            final CompletionHandler<Integer, AsynchronousSocketChannel> that = this;
                                            remoteSocketChannel.write(clientBuffer, remoteSocketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
                                                @Override
                                                public void completed(Integer result, AsynchronousSocketChannel remoteSocketChannel) {
                                                    clientBuffer.clear();
                                                    socketChannel.read(clientBuffer, remoteSocketChannel, that);
                                                }

                                                @Override
                                                public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                                                    logger.error(exc.getMessage(), exc);
                                                }
                                            });
                                        }

                                        @Override
                                        public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                                            logger.error(exc.getMessage(), exc);
                                        }
                                    });

                                    final ByteBuffer serverBuffer = ByteBuffer.allocate(1024);
                                    remoteSocketChannel.read(serverBuffer, socketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
                                        @Override
                                        public void completed(Integer result, final AsynchronousSocketChannel socketChannel) {
                                            if(result <=0 ) return;
                                            serverBuffer.flip();
                                            final CompletionHandler<Integer, AsynchronousSocketChannel> that = this;
                                            socketChannel.write(serverBuffer, socketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
                                                @Override
                                                public void completed(Integer result, AsynchronousSocketChannel remoteSocketChannel) {
                                                    serverBuffer.clear();
                                                    remoteSocketChannel.read(serverBuffer, socketChannel, that);
                                                }

                                                @Override
                                                public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                                                    logger.error(exc.getMessage(), exc);
                                                }
                                            });
                                        }

                                        @Override
                                        public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                                            logger.error(exc.getMessage(), exc);
                                        }
                                    });


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failed(Throwable exc, AsynchronousSocketChannel attachment) {

                            }
                        });

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

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        logger.error(exc.getMessage(), exc);
    }
}
