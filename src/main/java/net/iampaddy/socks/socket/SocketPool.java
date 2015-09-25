package net.iampaddy.socks.socket;

import net.iampaddy.pool.AbstractResourcePool;
import net.iampaddy.pool.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

/**
 * Description Here
 *
 * @author paddy.xie
 */
public class SocketPool extends AbstractResourcePool<Socket> {

    private Logger logger = LoggerFactory.getLogger(SocketPool.class);

    public SocketPool(ResourceFactory<Socket> factory) {
        super(factory);
    }


}
