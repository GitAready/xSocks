package net.iampaddy.socks.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.ReferenceCountUtil;
import net.iampaddy.socks.handler.FlushHandler;
import net.iampaddy.socks.handler.Socks5AuthHandler;
import net.iampaddy.socks.handler.Socks5CmdHandler;
import net.iampaddy.socks.message.SocksMessage;

import java.util.List;

/**
 * Created by Paddy on 3/10/2015.
 */
public class Socks5AuthCodec extends ByteToMessageCodec<Object> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        SocksMessage.AuthResponse message = (SocksMessage.AuthResponse) o;
        byteBuf.writeByte(message.getVersion());
        byteBuf.writeByte(message.getMethod());

        channelHandlerContext.pipeline().remove(Socks5AuthCodec.class.getName());
        channelHandlerContext.pipeline().remove(Socks5AuthHandler.class.getName());
        channelHandlerContext.pipeline()
                .addLast(Socks5CmdCodec.class.getName(), new Socks5CmdCodec())
                .addLast(Socks5CmdHandler.class.getName(), new Socks5CmdHandler());
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        byte version = byteBuf.readByte();
        byte method = byteBuf.readByte();
        byte[] methods = new byte[method];
        byteBuf.readBytes(methods);

        SocksMessage.AuthRequest message = new SocksMessage.AuthRequest(version, method, methods);

        list.add(message);

    }

}
