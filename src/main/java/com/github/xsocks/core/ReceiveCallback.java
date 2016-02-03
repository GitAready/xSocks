package com.github.xsocks.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by pxie on 1/27/16.
 */
public class ReceiveCallback implements CompletionHandler<Integer, ByteBuffer> {

    private Logger logger = LoggerFactory.getLogger(ReceiveCallback.class);

    private DataForwarder forwarder;

    private Session session;
    private AsynchronousSocketChannel remoteChannel;

    public ReceiveCallback(DataForwarder forwarder, Session session, AsynchronousSocketChannel remoteChannel) {
        this.forwarder = forwarder;
        this.session = session;
        this.remoteChannel = remoteChannel;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        if (result == -1) {
            forwarder.broken(session);
            logger.info("{} - remote channel closed", session.getDestKey());
            return;
        }
        buffer.flip();
        forwarder.forwardToAgent(session, buffer);
        buffer.clear();
        logger.trace("{} - receive data {}", session.getDestKey(), result);

        remoteChannel.read(buffer, buffer, this);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        forwarder.broken(session);
        logger.error("{} - read error: {}", session.getDestKey(), exc.getMessage());
        logger.error(exc.getMessage(), exc);
    }
}
