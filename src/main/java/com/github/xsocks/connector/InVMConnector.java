package com.github.xsocks.connector;

import com.github.xsocks.Configuration;
import com.github.xsocks.core.Session;
import com.github.xsocks.socket.DestKey;
import com.github.xsocks.socket.SocketManager;
import io.netty.buffer.ByteBuf;

/**
 * Description here
 *
 * @author paddy.xie
 */
public class InVMConnector implements Connector {


    public InVMConnector(Configuration conf) {

    }

    @Override
    public void connect() {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void writeTo(Session session, ByteBuf buffer) {

    }

    @Override
    public boolean resolve(DestKey destKey) {
        try {
            return SocketManager.getInstance().checkConnectivity(destKey);
        } catch (Exception e) {
            return false;
        }
    }

}
