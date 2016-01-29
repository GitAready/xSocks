package com.github.xsocks.core;

import io.netty.buffer.ByteBuf;
import com.github.xsocks.acceptor.Acceptor;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Paddy on 2016/1/26.
 */
public final class DataForwarder {

    private static final DataForwarder forwarder = new DataForwarder();
    private Lock lock;
    private Map<Session, Acceptor> acceptors;
    private Map<Session, AsynchronousSocketChannel> remoteChannels;

    private DataForwarder() {
        lock = new ReentrantLock(true);
        acceptors = new HashMap<>();
        remoteChannels = new HashMap<>();
    }

    public static DataForwarder getInstance() {
        return forwarder;
    }

    public boolean register(Session session, Acceptor ctx, AsynchronousSocketChannel channel) {
        lock.lock();
        acceptors.put(session, ctx);
        remoteChannels.put(session, channel);
        lock.unlock();
        return true;
    }

    public void unregister(Session session) {
        lock.lock();
        acceptors.remove(session);
        remoteChannels.remove(session);
        lock.unlock();
    }

    public void forwardToRemote(Session session, ByteBuf buffer) {

    }

    public void forwardToProxy(Session session, ByteBuffer buffer) {

    }

}
