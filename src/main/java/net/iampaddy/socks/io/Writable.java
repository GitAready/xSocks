package net.iampaddy.socks.io;

import io.netty.buffer.ByteBuf;

/**
 * Description here
 *
 * @author paddy.xie
 */
public interface Writable {

    void writeByte(byte b);

    void writeBytes(byte[] buffer);

    void writeBytes(byte[] buffer, int offset, int length);

    void writeAndFlush(ByteBuf buffer);

    void flush();

}
