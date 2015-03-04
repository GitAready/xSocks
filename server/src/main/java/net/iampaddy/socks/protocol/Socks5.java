package net.iampaddy.socks.protocol;

/**
 * Description
 *
 * @author paddy.xie
 */
public class Socks5 extends Socks {

    public final byte METHOD_NO_AUTH = 0x00;
    public final byte METHOD_GSSAPI = 0x01;
    public final byte METHOD_USER_PWD = 0x02;
    public final byte METHOD_RESERVED = (byte) 0x80;
    public final byte METHOD_NONE = (byte) 0xFF;



}
