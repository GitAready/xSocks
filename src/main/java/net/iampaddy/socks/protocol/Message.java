package net.iampaddy.socks.protocol;

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
    private byte[] buffer;

    public Message(String sourceAddress, int sourcePort, int targetAType, String targetAddress, int targetPort) {
        this.sourceAddress = sourceAddress;
        this.sourcePort = sourcePort;
        this.targetAType = targetAType;
        this.targetAddress = targetAddress;
        this.targetPort = targetPort;
    }

    public void write(byte[] content) {

    }

}
