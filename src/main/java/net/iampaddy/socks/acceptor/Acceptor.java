package net.iampaddy.socks.acceptor;

import net.iampaddy.socks.io.Readable;
import net.iampaddy.socks.io.Writable;

/**
 * Created by xpjsk on 2015/9/16.
 */
public interface Acceptor extends Readable, Writable {

    void accept();

    void close();

    void closeImmediately();

}
