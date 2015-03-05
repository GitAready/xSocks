package net.iampaddy.socks.protocol;

/**
 * Description
 *
 * @author paddy.xie
 */
public class Socks5 extends Socks4 {

    // Version
    public final static byte V5 = 0x05;

    public final byte METHOD_NO_AUTH = 0x00;
    public final byte METHOD_GSSAPI = 0x01;
    public final byte METHOD_USER_PWD = 0x02;
    public final byte METHOD_RESERVED = (byte) 0x80;
    public final byte METHOD_NONE = (byte) 0xFF;

    // Command
    public final static byte CMD_UDP_ASSOCIATE = 0x01;


}
