package net.iampaddy.socks.server;

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

    public Session createSession(ChannelHandlerContext context) {
        String sessionId;
        Session session;
        while (true) {
            sessionId = SessionIdGenerator.generate();
            session = new Session(sessionId, context);

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
            AsynchronousSocketChannel remoteChannel = session.getRemoteChannel();
            try {
                if (remoteChannel != null)
                    remoteChannel.close();
            } catch (IOException e) {
                // ignore
            }
            return;
        }
        throw new RuntimeException("Unknown session with id " + sessionId);
    }


}
