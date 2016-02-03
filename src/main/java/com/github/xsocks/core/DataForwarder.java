package com.github.xsocks.core;

import com.github.xsocks.acceptor.Acceptor;
import com.github.xsocks.socket.DestKey;
import com.github.xsocks.socket.SocketManager;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Paddy on 2016/1/26.
 */
public final class DataForwarder {

    private Logger logger = LoggerFactory.getLogger(DataForwarder.class);

    private static final DataForwarder forwarder = new DataForwarder();

    private ForwardCallback forwardCallback;

    private Lock lock;
    private Map<Session, Acceptor> acceptors;
    private Map<Session, AsynchronousSocketChannel> remoteChannels;

    private SocketManager socketManager = SocketManager.getInstance();
    private SessionManager sessionManager = SessionManager.getInstance();

    private DataForwarder() {
        lock = new ReentrantLock(true);

        forwardCallback = new ForwardCallback(this);

        acceptors = new HashMap<>();
        remoteChannels = new HashMap<>();
    }

    public static DataForwarder getInstance() {
        return forwarder;
    }

    public Session register(Acceptor ctx, DestKey destKey) {
        lock.lock();
        try {
            Session session = sessionManager.createSession(destKey);

            AsynchronousSocketChannel dest = socketManager.connect(destKey);

            ByteBuffer byteBuffer = ByteBuffer.allocate(10240);
            dest.read(byteBuffer, byteBuffer, new ReceiveCallback(this, session, dest));

            acceptors.put(session, ctx);
            remoteChannels.put(session, dest);

            return session;
        } finally {
            lock.unlock();
        }
    }

    public void unregister(Session session) {
        lock.lock();
        try {
            acceptors.remove(session);
            sessionManager.invalidateSession(session.getSessionId());
            AsynchronousSocketChannel channel = remoteChannels.remove(session);
            channel.close();
        } catch (IOException e) {
            // ignore
        } finally {
            lock.unlock();
        }
    }

    public void unregister(Acceptor acceptor) {
        lock.lock();
        try {
            List<Session> sessionsToUnregister = new ArrayList<>();
            for(Map.Entry<Session, Acceptor> entry : acceptors.entrySet()) {
                if(entry.getKey().equals(acceptor)) {
                    sessionsToUnregister.add(entry.getKey());
                }
            }

            for(Session session : sessionsToUnregister) {
                unregister(session);
            }
        } finally {
            lock.unlock();
        }
    }

    public void broken(Session session) {
        lock.lock();
        try {
            Acceptor acceptor = acceptors.get(session);
            unregister(session);
            acceptor.closeSession(session);
        } finally {
            lock.unlock();
        }
    }

    public Acceptor getAcceptor(Session session) {
        return acceptors.get(session);
    }

    public int forwardToRemote(Session session, ByteBuf byteBuf) {
        return forwardToRemote(session, byteBuf, byteBuf.readableBytes());
    }

    public int forwardToRemote(Session session, ByteBuf byteBuf, int length) {
        AsynchronousSocketChannel remoteChannel = remoteChannels.get(session);
        if (remoteChannel == null) {
            throw new RuntimeException("No Channel of session " + session + " was registered");
        }

        int lengthToWrite = byteBuf.readableBytes() > length ? length : byteBuf.readableBytes();
        remoteChannel.write(byteBuf.nioBuffer(byteBuf.readerIndex(), lengthToWrite), session, forwardCallback);

        byteBuf.skipBytes(lengthToWrite);

        logger.trace("{} - Forwarded {} bytes to {}", session.getSessionId(), lengthToWrite, remoteChannel);

        return lengthToWrite;
    }

    public void forwardToAgent(Session session, ByteBuffer buffer) {
        Acceptor acceptor = acceptors.get(session);
        if (acceptor == null) {
            throw new RuntimeException("No Acceptor of session " + session + " was registered");
        }
        acceptor.writeBack(session, buffer);
    }
}
