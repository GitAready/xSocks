package net.iampaddy.socks.socket;

import java.io.IOException;
import java.util.List;

/**
 * Created by xpjsk on 2015/9/14.
 */
public class SocketPool {

    private DestKey destKey;
    private List<Socket> sockets;

    public SocketPool(DestKey destKey) {
        this.destKey = destKey;

    }

    public Socket getOne() {
        try {
            java.net.Socket socket = new java.net.Socket(destKey.getAddress(), destKey.getPort());

            return new Socket(socket, this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void returnOne() {

    }

    public void init() {


    }
}
