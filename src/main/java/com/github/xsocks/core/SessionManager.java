package com.github.xsocks.core;

import com.github.xsocks.socket.DestKey;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by pxie on 1/28/16.
 */
public final class SessionManager {

    private static final SessionManager manager = new SessionManager();

    public static SessionManager getInstance() {
        return manager;
    }


    private ConcurrentMap<String, Session> sessions;

    private SessionManager() {
        sessions = new ConcurrentHashMap<>();
    }

    public Session getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public Session createSession(DestKey destKey) {
        String sessionId;
        Session session;
        while (true) {
            sessionId = SessionIdGenerator.generate();
            session = new Session(SessionIdGenerator.generate(), destKey);

            Session shouldBeNull = sessions.putIfAbsent(sessionId, session);
            if (shouldBeNull == null) {
                break;
            }
        }
        return session;
    }

    public void invalidateSession(String sessionId) {
        Session session = sessions.remove(sessionId);
        if (session != null) {
            return;
        }
        throw new RuntimeException("Unknown session with id " + sessionId);
    }


}
