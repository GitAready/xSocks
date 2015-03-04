package net.iampaddy.socks.protocol;

/**
 * Description
 *
 * @author paddy.xie
 */
public class Socks {

    public final static byte V4 = 0x04;
    public final static byte V5 = 0x05;

    public final static byte CMD_CONNECT = 0x01;
    public final static byte CMD_BIND = 0x02;
    public final static byte CMD_UDP_ASSOCIATE = 0x01;

    public final static byte ATYP_IP_V4 = 0x01;
    public final static byte ATYP_HOSTNAME=0x03;
    public final static byte ATYP_IP_V6 = 0x04;

    public final static byte REP_SUCCESS = 0x01;
    public final static byte REP_FAILURE = 0x02;
    public final static byte REP_NOT_ALLOWED = 0x03;
    public final static byte REP_NET_UNREACH = 0x04;
}
