package net.iampaddy.socks.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.iampaddy.socks.protocol.Socks;
import net.iampaddy.socks.socket.DestKey;
import net.iampaddy.socks.socket.Socket;
import net.iampaddy.socks.socket.SocketManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by xpjsk on 2015/9/15.
 */
public class Socks5Handler extends ChannelInboundHandlerAdapter {

    OutputStream os;
    InputStream is;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private State state = State.METHOD_SELECT;
    private SocketManager manager = SocketManager.getInstance();
    private int method = 0;
    private Socket socket;

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        ByteBuf b = ctx.alloc().buffer();
        switch (state) {
            case METHOD_SELECT:
                if (buf.readableBytes() <= 2) return;
                buf.readByte(); // version
                byte methods = buf.readByte();
                if (methods > buf.readableBytes()) return;
                byte[] methodArray = new byte[methods];
                buf.readBytes(methodArray);

                b.writeByte(Socks.V5);
                b.writeByte(Socks.METHOD_NO_AUTH);
                // method processing
                ctx.writeAndFlush(b);
                buf.release();
                method = methodArray[0];
                state = method == Socks.METHOD_NO_AUTH ? State.REQUEST : State.METHOD_PROCESS;
                break;
            case METHOD_PROCESS:
                if (method == Socks.METHOD_NO_AUTH) {
                    state = State.REQUEST;
                    return;
                }
                break;
            case REQUEST:
                if (buf.readableBytes() < 4) return;
                buf.readByte(); // version
                byte cmd = buf.readByte();
                buf.readByte(); // reserved
                byte atype = buf.readByte();
                String address;
                byte[] buffer;
                switch (atype) {
                    case Socks.ATYP_IP_V4:
                        buffer = new byte[4];
                        buf.readBytes(buffer);
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

                boolean connected = true;
                try {
                    socket = manager.connect(new DestKey(address, port));
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    connected = false;
                }

                b.writeByte(Socks.V5);
                if (!connected) {
                    b.writeByte(Socks.REP_HOST_UNREACHABLE);
                    b.writeByte(Socks.RESERVED);
                    b.writeByte(atype);
                    b.writeBytes(buffer);
                } else {
                    b.writeByte(Socks.REP_SUCCESS);
                    b.writeByte(Socks.RESERVED);
                    b.writeByte(Socks.ATYP_IP_V4);
                    b.writeBytes(socket.getSocket().getInetAddress().getAddress());
                }
                b.writeBytes(portBuf);
                ctx.writeAndFlush(b);
                buf.release();
                if (!connected) {
                    return;
                }
                state = State.CONNECT;
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            byte[] content = new byte[1024];
                            int length;
                            is = socket.getSocket().getInputStream();
                            while (true) {
                                if (is.available() > 0) {
                                    ByteBuf buf = ctx.alloc().buffer();
                                    length = is.read(content);
                                    buf.clear();
                                    buf.writeBytes(content, 0, length);
                                    ctx.writeAndFlush(buf);
                                } else {

                                    this.sleep(50);
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;
            case CONNECT:
                byte[] content = new byte[1024];
                os = socket.getSocket().getOutputStream();

                int length;
                while ((length = buf.readableBytes()) > 0) {
                    if (length > content.length) length = content.length;
                    buf.readBytes(content, 0, length);
                    os.write(content, 0, length);
                }
                os.flush();
                buf.release();
            default:
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    private enum State {
        METHOD_SELECT, METHOD_PROCESS, REQUEST, CONNECT
    }
}
