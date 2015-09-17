package net.iampaddy.socks.connector;


import net.iampaddy.socks.io.Writable;
import net.iampaddy.socks.io.Readable;

/**
 * Description here
 *
 * @author paddy.xie
 */
public interface Connector extends Readable, Writable {

    void connect();

    void disconnect();


}
