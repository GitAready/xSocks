package com.github.xsocks.protocol;

import io.netty.buffer.ByteBuf;

/**
 * Created by pxie on 1/28/16.
 */
public class ConnectRequest implements Message {

    private byte addressType;
    private String targetAddress;
    private int targetPort;

    public ConnectRequest(byte addressType) {
        this.addressType = addressType;
    }

    public ConnectRequest(byte addressType, String targetAddress, int targetPort) {
        this.addressType = addressType;
        this.targetAddress = targetAddress;
        this.targetPort = targetPort;
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
