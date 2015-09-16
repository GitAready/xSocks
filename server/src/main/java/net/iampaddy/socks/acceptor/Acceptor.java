package net.iampaddy.socks.acceptor;

import net.iampaddy.socks.io.*;

/**
 * Created by xpjsk on 2015/9/16.
 */
public interface Acceptor extends net.iampaddy.socks.io.Readable, Writable {

    void accept();

    void close();

    void closeImmediately();

}
