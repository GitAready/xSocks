package net.iampaddy.socks.acceptor;

import net.iampaddy.socks.io.Readable;
import net.iampaddy.socks.io.Writable;

/**
 * Description here
 *
 * @author paddy.xie
 */
public interface Acceptor extends Readable, Writable {

    void accept();

    void close();

    void closeImmediately();

}
