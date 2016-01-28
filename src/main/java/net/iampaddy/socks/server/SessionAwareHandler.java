package net.iampaddy.socks.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by pxie on 1/28/16.
 */
public abstract class SessionAwareHandler extends ChannelInboundHandlerAdapter {

    private SessionManager sessionManager = SessionManager.getInstance();

    private Status status = Status.SESSION;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf)msg;
        switch(status) {
            case SESSION:
                if(buffer.readableBytes() < 10) {

                }
                break;
        }
    }

    enum Status {
        SESSION
    }

}
