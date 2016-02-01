package com.github.xsocks.acceptor;

import com.github.xsocks.connector.InVMConnector;
import com.github.xsocks.core.Session;

import java.nio.ByteBuffer;

/**
 * Created by pxie on 1/29/16.
 */
public class InVMAcceptor extends AbstractAcceptor {

    private InVMConnector connector;

    @Override
    public void accept() {

    }

    @Override
    public void close() {

    }

    @Override
    public void write(Session session, ByteBuffer buffer) {

    }

    @Override
    public boolean isRemote() {
        return false;
    }
}
