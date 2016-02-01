package com.github.xsocks.core;

import io.netty.buffer.ByteBuf;
import com.github.xsocks.acceptor.Acceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private Logger logger = LoggerFactory.getLogger(DataForwarder.class);

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

    public void forwardToRemote(Session session, ByteBuffer buffer) {
        AsynchronousSocketChannel remoteChannel = remoteChannels.get(session);
        if(remoteChannel == null) {
            throw new RuntimeException("No Channel of session " + session + " was registered");
        }
        remoteChannel.write(buffer);
    }

    public void forwardToAgent(Session session, ByteBuffer buffer) {
        Acceptor acceptor = acceptors.get(session);
        if(acceptor == null) {
            throw new RuntimeException("No Acceptor of session " + session + " was registered");
        }
        acceptor.write(session, buffer);
    }

}
