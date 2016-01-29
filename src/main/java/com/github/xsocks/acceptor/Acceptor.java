package com.github.xsocks.acceptor;

import com.github.xsocks.core.Session;

import java.nio.ByteBuffer;

/**
 * Description here
 *
 * @author paddy.xie
 */
public interface Acceptor {

    void accept();

    void close();

    void write(Session session, ByteBuffer buffer);

    boolean isRemote();

}
