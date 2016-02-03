package com.github.xsocks.acceptor;

import com.github.xsocks.core.DataForwarder;
import com.github.xsocks.core.Session;
import com.github.xsocks.core.SessionManager;
import com.github.xsocks.protocol.Socks;
import com.github.xsocks.socket.DestKey;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class XSocksProcessHandler extends ChannelInboundHandlerAdapter {

    private SessionManager sessionManager = SessionManager.getInstance();

    protected Status status = Status.SESSION;

    private Session session;
    private int payloadLength;
    private int messageLength;

    private DataForwarder dataForwarder = DataForwarder.getInstance();
    private Acceptor acceptor;

    public XSocksProcessHandler(Acceptor acceptor) {
        this.acceptor = acceptor;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf) msg;
        messageLength = buffer.readInt();
        for (; ; ) {
            switch (status) {
                case COMMAND:
                    buffer.markReaderIndex();
                    byte cmd = buffer.readByte();
                    switch (cmd) {
                        case 0x01:
                            processConnectionRequest(ctx, buffer);
                            break;
                        case 0x02:
                            status = Status.SESSION;
                            break;
                        case 0x03:
                            break;
                        default:

                    }
                    break;
                case SESSION:
                    // 10 byte session id, 4 byte payload payloadLength
                    if (buffer.readableBytes() < 14) {
                        return;
                    }
                    byte[] sessionId = new byte[10];
                    buffer.readBytes(sessionId);
                    session = sessionManager.getSession(new String(sessionId));

                    payloadLength = buffer.readInt();
                    // valid payload
                    if (payloadLength > 0) {
                        status = Status.DATA_TRANS;
                    }
                    break;
                case DATA_TRANS:
                    int lengthWritten = dataForwarder.forwardToRemote(session, buffer, payloadLength);
                    if (lengthWritten == 0) {
                        // no more data available, return to get more
                        return;
                    } else if (lengthWritten < payloadLength) {
                        payloadLength = payloadLength - lengthWritten;
                    } else {
                        status = Status.SESSION;
                        payloadLength = -1;
                    }
            }
        }
    }

    private void processConnectionRequest(ChannelHandlerContext ctx, ByteBuf buf) {
        byte atype = buf.readByte();
        String address;
        byte[] buffer;
        switch (atype) {
            case Socks.ATYP_IP_V4:
                buffer = new byte[4];
                break;
            case Socks.ATYP_HOSTNAME:
                short length = buf.readUnsignedByte();
                buffer = new byte[length];
                break;
            case Socks.ATYP_IP_V6:
                buffer = new byte[16];
                break;
            default:
                throw new RuntimeException("Invalid Address Type");
        }
        buf.readBytes(buffer);
        address = new String(buffer);
        byte[] portBuf = new byte[2];
        buf.readBytes(portBuf);
        int port = ((portBuf[0] & 0x0FF) << 8) + (portBuf[1] & 0x0FF);


        DestKey destKey = new DestKey(address, port);
        Session session = dataForwarder.register(acceptor, destKey);

        ByteBuf b = ctx.alloc().buffer();
        b.writeByte(Socks.V5);
        b.writeByte(Socks.REP_SUCCESS);
        b.writeByte(Socks.RESERVED);
        b.writeByte(atype);
        if (atype == Socks.ATYP_HOSTNAME) {
            b.writeByte(buffer.length);
        }
        b.writeBytes(buffer);
        b.writeBytes(portBuf);

        ctx.writeAndFlush(b);
        buf.release();
    }

    enum Status {
        // 1 byte cmd, 0x01 create session, 0x02 data transfer, 0x03 destroy session
        COMMAND,
        // 10 byte session id, 4 byte payload payloadLength
        SESSION,
        // all data
        DATA_TRANS
    }

}
