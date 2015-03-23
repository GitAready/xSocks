package net.iampaddy.socks.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.iampaddy.socks.message.SocksMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Paddy on 3/10/2015.
 */
public class Socks5AuthHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(Socks5AuthHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        logger.info(ctx.toString());
        ctx.write(new SocksMessage.AuthResponse((byte) 5, (byte) 0));

    }
}
