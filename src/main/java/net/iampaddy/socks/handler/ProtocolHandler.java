package net.iampaddy.socks.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.iampaddy.socks.protocol.Socks;

/**
 * Description here
 *
 * @author paddy.xie
 */
public class ProtocolHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        // mark it in case it's unknown protocol
        buf.markReaderIndex();
        int version = buf.readUnsignedByte();

        if (version == Socks.V4) {
            ctx.pipeline().addLast(Socks4Handler.class.getName(), new Socks4Handler());
            ctx.pipeline().remove(this);
        } else if (version == Socks.V5) {
            ctx.pipeline().addLast(Socks5Handler.class.getName(), new Socks5Handler());
            ctx.pipeline().remove(this);
        } else {
            buf.resetReaderIndex();
        }

        ctx.fireChannelRead(msg);
    }
}
