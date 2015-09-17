package net.iampaddy.socks.connector;

import io.netty.buffer.ByteBuf;

import java.util.Properties;

/**
 * Description here
 *
 * @author paddy.xie
 */
public class InVMConnector implements Connector {


    public InVMConnector(Properties prop) {

    }

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
