package net.iampaddy.socks.acceptor;

/**
 * Description here
 *
 * @author paddy.xie
 */
public interface Acceptor {

    void accept();

    void close();

    void closeImmediately();

}
