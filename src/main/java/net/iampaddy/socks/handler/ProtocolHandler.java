package net.iampaddy.socks.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.iampaddy.socks.protocol.Socks;

/**
 * Created by xpjsk on 2015/9/15.
 */
public class ProtocolHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        buf.markReaderIndex();
        int version = buf.readUnsignedByte();
        if (version == Socks.V4) {
            ctx.pipeline().addLast(Socks4Handler.class.getName(), new Socks4Handler());
        } else if (version == Socks.V5) {
            ctx.pipeline().addLast(Socks5Handler.class.getName(), new Socks5Handler());
        }
        buf.resetReaderIndex();
        ctx.pipeline().remove(this);

        ctx.fireChannelRead(msg);
    }
}
