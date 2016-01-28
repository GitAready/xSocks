package net.iampaddy.socks.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.iampaddy.socks.protocol.Socks;
import net.iampaddy.socks.socket.DestKey;
import net.iampaddy.socks.socket.RemoteReadHandler;
import net.iampaddy.socks.socket.SocketManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Description here
 *
 * @author paddy.xie
 */
public class Socks5Handler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private State state = State.METHOD_SELECT;
    private SocketManager manager = SocketManager.getInstance();
    private int method = 0;

    private DestKey destKey;
    private AsynchronousSocketChannel remoteChannel;

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        for (; ; ) {
            ByteBuf b = ctx.alloc().buffer();
            switch (state) {
                case METHOD_SELECT:
                    // auth method number
                    byte methods = buf.readByte();
                    // auth method types
                    byte[] methodArray = new byte[methods];
                    buf.readBytes(methodArray);

                    // for now, skip method
                    b.writeByte(Socks.V5);
                    b.writeByte(Socks.METHOD_NO_AUTH);

                    ctx.writeAndFlush(b);
                    buf.release();

                    method = methodArray[0];
                    state = method == Socks.METHOD_NO_AUTH ? State.REQUEST : State.METHOD_PROCESS;

                    logger.debug("socks auth method selected");
                    break;
                case METHOD_PROCESS:
                    if (method == Socks.METHOD_NO_AUTH) {
                        state = State.REQUEST;
                        return;
                    }
                    break;
                case REQUEST:
                    if (buf.readableBytes() < 8) return;

                    buf.readByte(); // version
                    byte cmd = buf.readByte();
                    buf.readByte(); // reserved
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

                    boolean connected = true;
                    try {
                        destKey = new DestKey(address, port);
                        remoteChannel = manager.connect(destKey);
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
                        b.writeBytes(((InetSocketAddress) remoteChannel.getRemoteAddress()).getAddress().getAddress());
                    }
                    b.writeBytes(portBuf);
                    ctx.writeAndFlush(b);
                    buf.release();
                    if (!connected) {
                        return;
                    }
                    state = State.CONNECT;
                    final ByteBuffer byteBuffer = ByteBuffer.allocate(10240);
                    remoteChannel.read(byteBuffer, byteBuffer, new RemoteReadHandler(destKey, ctx, remoteChannel));
                    logger.debug("{} - socks request completed", destKey);
                    break;
                case CONNECT:
                    if (buf.readableBytes() > 0) {
                        try {
                            logger.debug("{} - sending data {}", destKey, buf.readableBytes());
                            remoteChannel.write(buf.nioBuffer()).get();
                            buf.clear();
                            buf.release();
                        } catch (Exception e) {
                            ctx.close();
                            logger.error("{} - sending data error: {}", destKey, e.getMessage());
                            logger.error(e.getMessage(), e);
                            return;
                        }
                    } else {
                        return;
                    }
                default:
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        manager.disconnect(destKey, remoteChannel);
        info("channel inactive");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        info("channel unregistered");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        info("channel registered");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        info("channel active");
    }

    private void info(String message) {
        logger.info("{} - " + message, destKey);
    }

    private enum State {
        METHOD_SELECT, METHOD_PROCESS, REQUEST, CONNECT
    }
}
