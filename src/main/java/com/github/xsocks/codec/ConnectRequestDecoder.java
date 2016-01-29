package com.github.xsocks.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import com.github.xsocks.protocol.ConnectRequest;
import com.github.xsocks.protocol.Socks;

import java.util.List;

/**
 * Created by pxie on 1/28/16.
 */
public class ConnectRequestDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {

        byte addressType = buf.readByte();

        byte[] buffer;
        switch (addressType) {
            case Socks.ATYP_IP_V4:
                buffer = new byte[4];
                break;
            case Socks.ATYP_HOSTNAME:
                short length = buf.readUnsignedByte();
                buffer = new byte[length];
                break;
            case Socks.ATYP_IP_V6:
                buffer = new byte[16];
                break;
            default:
                out.add(new ConnectRequest(addressType));
                buf.clear(); // clear dirty data
                return;
        }

        buf.readBytes(buffer);
        String targetAddress = new String(buffer);
        byte[] portBuf = new byte[2];
        buf.readBytes(portBuf);
        int targetPort = ((portBuf[0] & 0x0FF) << 8) + (portBuf[1] & 0x0FF);

        out.add(new ConnectRequest(addressType, targetAddress, targetPort));

    }
}
