package net.iampaddy.socks.io;

import io.netty.buffer.ByteBuf;

/**
 * Created by xpjsk on 2015/9/16.
 */
public interface Readable {

    int read(ByteBuf buffer);

    int read(byte[] buffer);

}
