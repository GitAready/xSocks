package net.iampaddy.socks.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Paddy on 3/11/2015.
 */
public class RemoteHandler extends ChannelInboundHandlerAdapter {

    private Socket socket = null;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(this.socket == null) socket = (Socket)ctx.attr(AttributeKey.newInstance("socket")).get();

        if(socket == null) return;

        OutputStream os = socket.getOutputStream();

        ByteBuf byteBuf = (ByteBuf)msg;
        byte[] buffer = new byte[1024];
        int length;
        while((length = byteBuf.readableBytes()) > 0) {
            if(length > 1024) {
                length = 1024;
            }
            byteBuf.readBytes(buffer, 0, length);
            os.write(buffer, 0, length);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        InputStream is = socket.getInputStream();
    }
}
