package net.iampaddy.socks.io;

import io.netty.buffer.ByteBuf;

/**
 * Created by xpjsk on 2015/9/16.
 */
public interface Writable {

    void writeByte(byte b);

    void writeBytes(byte[] buffer);

    void writeBytes(byte[] buffer, int offset, int length);

    void writeAndFlush(ByteBuf buffer);

    void flush();

}
