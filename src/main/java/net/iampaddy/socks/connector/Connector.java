package net.iampaddy.socks.connector;


import net.iampaddy.socks.io.Writable;
import net.iampaddy.socks.io.Readable;

/**
 * Created by xpjsk on 2015/9/16.
 */
public interface Connector extends Readable, Writable {

    void connect();

    void disconnect();


}
