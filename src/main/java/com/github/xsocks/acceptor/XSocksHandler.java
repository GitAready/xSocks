package com.github.xsocks.acceptor;

import com.github.xsocks.core.SessionAwareHandler;
import com.github.xsocks.protocol.Socks;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by pxie on 1/28/16.
 */
public class XSocksHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(XSocksHandler.class);

    private static final byte METHOD_SELECTION = Socks.METHOD_NO_AUTH;

    private String remoteAddress;

    private byte status = METHOD_SELECTION;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        remoteAddress = ((InetSocketAddress) socketAddress).getHostString();

        logger.info("Connection from {} connected", remoteAddress);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Connection from {} disconnected", remoteAddress);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        ByteBuf result = ctx.alloc().buffer();
        switch (status) {
            case METHOD_SELECTION:
                byte methodNumber = buf.readByte();
                byte[] methods = new byte[methodNumber];
                buf.readBytes(methods);

                // default method none
                byte method = Socks.METHOD_NONE;
                for (byte pm : Socks.METHODS) {
                    for (byte m : methods) {
                        if (pm == m) {
                            method = m;
                        }
                    }
                }

                // write the selected method back, no matter none or some other method
                result.writeByte(method);
                ctx.writeAndFlush(result);

                if (method == Socks.METHOD_NONE) {
                    ctx.close();
                }
                // switch to next method processing status
                status = method;
                break;
            case Socks.METHOD_USER_PWD:
                byte usernameLength = buf.readByte();
                byte[] username = new byte[usernameLength];
                buf.readBytes(username);
                byte passwordLength = buf.readByte();
                byte[] password = new byte[passwordLength];
                buf.readBytes(password);

                if ("paddy".equals(new String(username)) && "62361024".equals(new String(password))) {
                    result.writeByte(Socks.REP_SUCCESS);
                    ctx.writeAndFlush(result);

                    ctx.pipeline().remove(this);
                    ctx.pipeline().addLast("SessionAwareHandler", new SessionAwareHandler());

                    ctx.fireChannelRead(msg);
                } else {
                    result.writeByte(Socks.REP_FAILURE);
                    ctx.writeAndFlush(result);
                }

                break;
            default:
                result.writeByte(Socks.METHOD_NONE);
                ctx.writeAndFlush(result);
                ctx.close();
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Error occur in connection from {}", remoteAddress);
        logger.error(cause.getMessage(), cause);
    }


}
