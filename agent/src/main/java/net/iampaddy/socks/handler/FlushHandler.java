package net.iampaddy.socks.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import net.iampaddy.socks.codec.Socks5CmdCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Paddy on 3/11/2015.
 */
public class FlushHandler extends ChannelOutboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(FlushHandler.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        logger.info(this.toString());
        logger.info(ctx.channel().toString());
        ctx.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
