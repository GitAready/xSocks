package net.iampaddy.socks.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import net.iampaddy.socks.message.SocksMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

/**
 * Created by Paddy on 3/11/2015.
 */
public class Socks5CmdHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(Socks5CmdHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info(ctx.channel().toString());
        SocksMessage.CommandRequest request = (SocksMessage.CommandRequest)msg;

        SocksMessage.CommandResponse response = new SocksMessage.CommandResponse(request.getVersion(), (byte)0,
                request.getAddressType(), request.getAddressBytes(), request.getPortBytes());

//        Socket socket = new Socket(request.getAddress(), request.getPort());
//
//        ctx.attr(AttributeKey.newInstance("socket")).set(socket);

        ctx.write(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info(ctx.toString());
        super.channelReadComplete(ctx);
    }
}
