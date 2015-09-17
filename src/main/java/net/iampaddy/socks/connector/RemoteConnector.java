package net.iampaddy.socks.connector;

import io.netty.buffer.ByteBuf;

/**
 * Description here
 *
 * @author paddy.xie
 */
public class RemoteConnector implements Connector {
    @Override
    public void connect() {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public int read(ByteBuf buffer) {
        return 0;
    }

    @Override
    public int read(byte[] buffer) {
        return 0;
    }

    @Override
    public void writeByte(byte b) {

    }

    @Override
    public void writeBytes(byte[] buffer) {

    }

    @Override
    public void writeBytes(byte[] buffer, int offset, int length) {

    }

    @Override
    public void writeAndFlush(ByteBuf buffer) {

    }

    @Override
    public void flush() {

    }
}
