package net.iampaddy.socks.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Paddy on 3/11/2015.
 */
public class RemoteHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.channel();
    }
}
