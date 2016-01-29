package com.github.xsocks.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by pxie on 1/28/16.
 */
public class Session {

    private Logger logger = LoggerFactory.getLogger(Session.class);

    private String sessionId;
    private long createdOn;
    private long lastAccess;

    private String remoteAddress;


    private ChannelHandlerContext context;

    private AsynchronousSocketChannel remoteChannel;

    protected Session(String sessionId, ChannelHandlerContext context) {
        this.sessionId = sessionId;
        this.context = context;
    }

    public String getSessionId() {
        return sessionId;
    }

    public long getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(long lastAccess) {
        this.lastAccess = lastAccess;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public ChannelHandlerContext getContext() {
        return context;
    }

    public AsynchronousSocketChannel getRemoteChannel() {
        return remoteChannel;
    }

    public void setRemoteChannel(AsynchronousSocketChannel remoteChannel) {
        this.remoteChannel = remoteChannel;
    }

    public int sendToRemote(ByteBuf byteBuf, int length) {
        int lengthToWrite = byteBuf.readableBytes() > length ? length : byteBuf.readableBytes();

        remoteChannel.write(byteBuf.nioBuffer(byteBuf.readerIndex(), lengthToWrite));

        logger.debug("{} - Forwarded {} bytes to {}", sessionId, lengthToWrite, remoteChannel);

        byteBuf.skipBytes(lengthToWrite);
        return lengthToWrite;
    }

    public void sendToProxy(ByteBuffer byteBuffer) {
        ByteBuf buffer = context.alloc().buffer(byteBuffer.remaining());
        byteBuffer.flip();
        buffer.writeBytes(byteBuffer);
        context.writeAndFlush(buffer);
    }

}
