package net.iampaddy.socks.protocol;

import io.netty.buffer.ByteBuf;

/**
 * Created by pxie on 1/28/16.
 */
public class ConnectResponse implements Message{

    public final static ConnectResponse UNKNOWN_ATYPE = new ConnectResponse(Socks.REP_UNKNOWN_ATYP);

    private byte responseType;
    private byte addressType;
    private String targetAddress;
    private int targetPort;

    public ConnectResponse(byte responseType) {
        this.responseType = responseType;
    }

    public ConnectResponse(byte responseType, byte addressType, String targetAddress, int targetPort) {
        this.responseType = responseType;
        this.addressType = addressType;
        this.targetAddress = targetAddress;
        this.targetPort = targetPort;
    }

    public byte getResponseType() {
        return responseType;
    }

    public byte getAddressType() {
        return addressType;
    }

    public String getTargetAddress() {
        return targetAddress;
    }

    public int getTargetPort() {
        return targetPort;
    }

    @Override
    public void encodeAsByteBuf(ByteBuf byteBuf) {
        byteBuf.writeByte(responseType);
        byteBuf.writeByte(Socks.RESERVED);
        if(responseType == Socks.REP_UNKNOWN_ATYP) {
            // address type
            byteBuf.writeByte(addressType);
            // TODO fill in the left bytes after an unknown address type
        } else {
            byteBuf.writeByte(addressType);
            if(addressType == Socks.ATYP_HOSTNAME) {
                byteBuf.writeByte(targetAddress.getBytes().length);
            }
            byteBuf.writeBytes(targetAddress.getBytes());
            // port  -  two bytes
            byteBuf.writeByte((byte) (targetPort >> 8 & 0xff));
            byteBuf.writeByte((byte) (targetPort & 0xff));
        }
    }
}
