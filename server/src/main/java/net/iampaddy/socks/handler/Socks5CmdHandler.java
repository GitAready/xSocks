package net.iampaddy.socks.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.iampaddy.socks.message.SocksMessage;

/**
 * Created by Paddy on 3/11/2015.
 */
public class Socks5CmdHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SocksMessage.CommandRequest request = (SocksMessage.CommandRequest)msg;

        SocksMessage.CommandResponse response = new SocksMessage.CommandResponse(request.getVersion(), (byte)0,
                request.getAddressType(), request.getAddressBytes(), request.getPortBytes());



        ctx.write(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
}
