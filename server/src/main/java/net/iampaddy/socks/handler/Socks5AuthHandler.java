package net.iampaddy.socks.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.iampaddy.socks.message.SocksMessage;

/**
 * Created by Paddy on 3/10/2015.
 */
public class Socks5AuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ctx.write(new SocksMessage.AuthResponse((byte) 5, (byte) 0));

    }
}
