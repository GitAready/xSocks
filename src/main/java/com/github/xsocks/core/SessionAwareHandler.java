package com.github.xsocks.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by pxie on 1/28/16.
 */
public class SessionAwareHandler extends ChannelInboundHandlerAdapter {

    private SessionManager sessionManager = SessionManager.getInstance();

    protected Status status = Status.SESSION;

    private Session session;
    private int length;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf) msg;
        for (; ; ) {
            switch (status) {
                case COMMAND:
                    byte cmd = buffer.readByte();
                    switch(cmd) {
                        case 0x01:
                            break;
                        case 0x02:
                            status = Status.SESSION;
                            break;
                        case 0x03:
                            break;
                        default:

                    }

                case SESSION:
                    // 10 byte session id, 4 byte payload length
                    if (buffer.readableBytes() < 14) {
                        return;
                    }
                    byte[] sessionId = new byte[10];
                    buffer.readBytes(sessionId);
                    session = sessionManager.getSession(new String(sessionId));

                    length = buffer.readInt();
                    // valid payload
                    if (length > 0) {
                        status = Status.DATA_TRANS;
                    }
                    break;
                case DATA_TRANS:
                    int lengthWritten = session.sendToRemote(buffer, length);
                    if (lengthWritten == 0) {
                        // no more data available, return to get more
                        return;
                    } else if (lengthWritten < length) {
                        length = length - lengthWritten;
                    } else {
                        status = Status.SESSION;
                        length = -1;
                    }
            }
        }
    }

    enum Status {
        // 1 byte cmd, 0x01 create session, 0x02 data transfer, 0x03 destroy session
        COMMAND,
        // 10 byte session id, 4 byte payload length
        SESSION,
        // all data
        DATA_TRANS
    }

}
