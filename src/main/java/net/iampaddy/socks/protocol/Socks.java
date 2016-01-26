package net.iampaddy.socks.protocol;

/**
 * Description
 *
 * @author paddy.xie
 */
public final class Socks {

    // Version
    public final static byte V4 = 0x04;
    public final static byte V5 = 0x05;

    public final static byte RESERVED = 0x00;
    public final static byte NULL = 0x00;
    public final static byte NOT_NULL = 0x01;

    // Command
    public final static byte CMD_CONNECT = 0x01;
    public final static byte CMD_BIND = 0x02;
    public final static byte CMD_UDP_ASSOCIATE = 0x01;

    // Status
    public final static byte STATUS_GRANTED = 0x5a;
    public final static byte STATUS_REJECTED = 0x5b;
    public final static byte STATUS_NO_IDENTD = 0x5c;
    public final static byte STATUS_INVALID_USER = 0x5d;

    // Address Type
    public final static byte ATYP_IP_V4 = 0x01;
    public final static byte ATYP_HOSTNAME = 0x03;
    public final static byte ATYP_IP_V6 = 0x04;

    // Reply field
    // 0x00 - 0x08 Defined
    // 0x09 - 0xFF Unassigned
    public final static byte REP_SUCCESS = 0x00;
    public final static byte REP_FAILURE = 0x01;
    public final static byte REP_NOT_ALLOWED = 0x02;
    public final static byte REP_NET_UNREACHABLE = 0x03;
    public final static byte REP_HOST_UNREACHABLE = 0x04;
    public final static byte REP_CONN_REFUSED = 0x05;
    public final static byte REP_TTL_EXPIRED = 0x06;
    public final static byte REP_UNKNOWN_CMD = 0x07;
    public final static byte REP_UNKNOWN_ATYP = 0x08;

    // Method
    public final static byte METHOD_NO_AUTH = 0x00;
    public final static byte METHOD_GSSAPI = 0x01;
    public final static byte METHOD_USER_PWD = 0x02;
    public final static byte METHOD_RESERVED = (byte) 0x80;
    public final static byte METHOD_NONE = (byte) 0xFF;

}
