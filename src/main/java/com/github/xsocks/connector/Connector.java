package com.github.xsocks.connector;


import com.github.xsocks.core.Session;
import com.github.xsocks.socket.DestKey;
import io.netty.buffer.ByteBuf;

/**
 * Description here
 *
 * @author paddy.xie
 */
public interface Connector {

    void connect();

    void disconnect();

    void writeBack(Session session, ByteBuf buffer);

    boolean resolve(DestKey destKey);

}
