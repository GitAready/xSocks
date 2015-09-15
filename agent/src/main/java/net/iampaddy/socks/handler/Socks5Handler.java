package net.iampaddy.socks.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.iampaddy.socks.socket.DestKey;
import net.iampaddy.socks.socket.Socket;
import net.iampaddy.socks.socket.SocketManager;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by xpjsk on 2015/9/15.
 */
public class Socks5Handler extends ChannelInboundHandlerAdapter {


    private enum State {
        METHOD_SELECT, METHOD_PROCESS, REQUEST, CONNECT
    }

    private State state = State.METHOD_SELECT;
    private SocketManager manager = new SocketManager();

    private int method = 0;
    private Socket socket;
    OutputStream os;
    InputStream is;

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

                b.writeByte(5);
                b.writeByte(0);
                // method processing
                ctx.write(b);
                buf.release();
                method = methodArray[0];
                state = method == 0 ? State.REQUEST : State.METHOD_PROCESS;
                break;
            case METHOD_PROCESS:
                 if(method == 0) {
                     state = State.REQUEST;
                     return;
                 }
            case REQUEST:
                if(buf.readableBytes() < 4) return;
                buf.readByte(); // version
                byte cmd = buf.readByte();
                buf.readByte(); // reserved
                byte atype = buf.readByte();
                String address;
                byte[] buffer;
                switch (atype) {
                    case 0x01:
                        buffer = new byte[4];
                        buf.readBytes(buffer);
                        break;
                    case 0x03:
                        short length = buf.readUnsignedByte();
                        buffer = new byte[length];
                        break;
                    case 0x04:
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

                socket = manager.connect(new DestKey(address, port));
                b.writeByte(5);
                b.writeByte(0);
                b.writeByte(0);
                b.writeByte(1);
                b.writeBytes(socket.getSocket().getInetAddress().getAddress());
                b.writeBytes(portBuf);
                ctx.write(b);
                buf.release();
                state = State.CONNECT;
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            byte[] content = new byte[1024];
                            int length;
                            is = socket.getSocket().getInputStream();
                            while(true) {
                                if (is.available() > 0) {
                                    ByteBuf buf = ctx.alloc().buffer();
                                    length = is.read(content);
                                    String cont = new String(content, 0, length);
                                    System.out.println(cont);
                                    buf.clear();
                                    buf.writeBytes(cont.getBytes());
                                    ctx.write(buf);
                                }

                                this.sleep(500);
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;
            case CONNECT:
                byte[] content = new byte[1024];
                int length = buf.readableBytes();
                buf.readBytes(content, 0, length);
                os = socket.getSocket().getOutputStream();
                os.write(content, 0, length);
                os.flush();
                buf.release();
            default:
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
}
