package net.iampaddy.socks.socket;

import net.iampaddy.pool.Resource;
import net.iampaddy.pool.ResourceFactory;

import java.io.IOException;
import java.net.Socket;

/**
 * Description Here
 *
 * @author paddy.xie
 */
public class SocketFactory implements ResourceFactory<Socket> {

    private DestKey destKey;

    public SocketFactory(DestKey destKey) {
        this.destKey = destKey;
    }

    @Override
    public Resource<Socket> create() {
        return new SocketResource(destKey);
    }
}
