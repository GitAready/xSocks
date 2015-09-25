package net.iampaddy.socks.socket;

import net.iampaddy.pool.Resource;

import java.io.IOException;
import java.net.Socket;

/**
 * Description Here
 *
 * @author paddy.xie
 */
public class SocketResource implements Resource<Socket> {

    private DestKey destKey;

    public SocketResource(DestKey destKey) {
        this.destKey = destKey;
    }

    @Override
    public void init() {
        try {
            Socket socket = new Socket(destKey.getAddress(), destKey.getPort());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {

    }
}
