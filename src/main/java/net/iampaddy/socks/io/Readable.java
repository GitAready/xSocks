package net.iampaddy.socks.io;

import io.netty.buffer.ByteBuf;

/**
 * Description here
 *
 * @author paddy.xie
 */
public interface Readable {

    int read(ByteBuf buffer);

    int read(byte[] buffer);

}
