package net.iampaddy.socks.server;

import io.netty.channel.ChannelHandlerContext;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by pxie on 1/28/16.
 */
public class Session {

    private String sessionId;
    private long createdOn;
    private long lastAccess;


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

}
