package com.github.xsocks.acceptor;

import com.github.xsocks.core.Session;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by pxie on 2/1/16.
 */
public abstract class AbstractAcceptor implements Acceptor {

    private final ConcurrentMap<Session, ChannelHandlerContext> clientMap = new ConcurrentHashMap<>();


    public boolean addSession(Session session, ChannelHandlerContext context) {
        return clientMap.putIfAbsent(session, context) == null;
    }

    public void removeSession(Session session) {
        ChannelHandlerContext context = clientMap.get(session);
        context.close();
    }

    @Override
    public void write(Session session, ByteBuffer buffer) {
        ChannelHandlerContext context = clientMap.get(session);
        if(context == null) {
            throw new RuntimeException("No agent channel found for session " + session);
        }

        ByteBuf byteBuf = context.alloc().buffer(buffer.capacity());
        byteBuf.writeBytes(buffer);
        context.writeAndFlush(byteBuf);
    }
}
