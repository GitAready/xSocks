package net.iampaddy.socks.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by xpjsk on 2015/9/14.
 */
public class SocketPool {

    private Logger logger = LoggerFactory.getLogger(SocketPool.class);

    private DestKey destKey;
    private List<Socket> sockets;

    public SocketPool(DestKey destKey) {
        this.destKey = destKey;
    }

    public void init() {
    }

    public Socket connect() {
        try {
            logger.info("Create socket connection to " + destKey);
            java.net.Socket socket = new java.net.Socket(destKey.getAddress(), destKey.getPort());

            return new Socket(socket, this);
        } catch (IOException e) {
            throw new RuntimeException(destKey.toString(), e);
        }
    }

    public void freeOne() {

    }

}
