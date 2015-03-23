package net.iampaddy.socks.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.ReferenceCountUtil;
import net.iampaddy.socks.handler.RemoteHandler;
import net.iampaddy.socks.handler.Socks5CmdHandler;
import net.iampaddy.socks.message.SocksMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Paddy on 3/11/2015.
 */
public class Socks5CmdCodec extends ByteToMessageCodec<Object> {

    private Logger logger = LoggerFactory.getLogger(Socks5CmdCodec.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        logger.info(channelHandlerContext.channel().toString());
        SocksMessage.CommandResponse response = (SocksMessage.CommandResponse)o;
        byteBuf.writeByte(response.getVersion());
        byteBuf.writeByte(response.getReply());
        byteBuf.writeByte(response.getReserved());
        byteBuf.writeByte(response.getAddressType());
        if(response.getAddressType() == 3) {
            byteBuf.writeByte(response.getAddressBytes().length);
        }
        byteBuf.writeBytes(response.getAddressBytes());
        byteBuf.writeBytes(response.getPortBytes());

        channelHandlerContext.pipeline().remove(Socks5CmdCodec.class.getName());
        channelHandlerContext.pipeline().remove(Socks5CmdHandler.class.getName());
        channelHandlerContext.pipeline()
                .addLast(RemoteHandler.class.getName(), new RemoteHandler());
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        logger.info(channelHandlerContext.channel().toString());

        byte version = (byte)byteBuf.readUnsignedByte();
        byte command = byteBuf.readByte();
        byteBuf.readByte(); // reserved
        byte addressType = byteBuf.readByte();
        byte[] addressBytes = null;
        switch(addressType) {
            case 0x01:
                addressBytes = new byte[4];
                byteBuf.readBytes(addressBytes);
                break;
            case 0x03:
                byte length = byteBuf.readByte();
                addressBytes = new byte[length];
                byteBuf.readBytes(addressBytes);
                break;
            case 0x04:
                addressBytes = new byte[16];
                byteBuf.readBytes(addressBytes);
                break;
            default:
                addressType = -1;
        }
        byte[] portBytes = new byte[2];
        byteBuf.readBytes(portBytes);

        SocksMessage.CommandRequest request = new SocksMessage.CommandRequest(version, command, addressType, addressBytes, portBytes);
        list.add(request);

        logger.info("Message : " + request.getAddress());
    }
}
