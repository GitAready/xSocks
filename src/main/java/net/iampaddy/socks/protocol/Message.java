package net.iampaddy.socks.protocol;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Description here
 *
 * @author paddy.xie
 */
public interface Message {

    public void encodeAsByteBuf(ByteBuf byteBuf);

}
