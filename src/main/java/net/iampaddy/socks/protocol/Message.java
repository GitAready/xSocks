package net.iampaddy.socks.protocol;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Description here
 *
 * @author paddy.xie
 */
public class Message {

    private String sourceAddress;
    private int sourcePort;

    private int targetAType;
    private String targetAddress;
    private int targetPort;

    private int length;
    private byte[] content;

    private ByteArrayOutputStream buffer;

    public Message(String sourceAddress, int sourcePort, int targetAType, String targetAddress, int targetPort) {
        this.sourceAddress = sourceAddress;
        this.sourcePort = sourcePort;
        this.targetAType = targetAType;
        this.targetAddress = targetAddress;
        this.targetPort = targetPort;

        this.buffer = new ByteArrayOutputStream();
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public int getTargetAType() {
        return targetAType;
    }

    public String getTargetAddress() {
        return targetAddress;
    }

    public int getTargetPort() {
        return targetPort;
    }

    public int getLength() {
        return length;
    }

    public byte[] getContent() {
        return content;
    }

    public void write(byte[] content) {
        try {
            this.buffer.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void write(byte[] content, int offset, int length) {
        this.buffer.write(content, offset, length);
    }

    public void write(ByteBuf byteBuf) {
        byte[] buf = new byte[1024];
        int length;
        while ((length = byteBuf.readableBytes()) > 0) {
            if(length > buf.length) length = buf.length;
            byteBuf.readBytes(buf, 0, length);
            this.buffer.write(buf, 0, length);
        }
    }

    public Message readMessage(ByteBuf buf) {
        return null;
    }
}
