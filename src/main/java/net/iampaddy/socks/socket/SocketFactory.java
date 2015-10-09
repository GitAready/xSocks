package net.iampaddy.socks.socket;

import net.iampaddy.pool.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

/**
 * Description Here
 *
 * @author paddy.xie
 */
public class SocketFactory implements ResourceFactory<Socket> {

    private Logger logger = LoggerFactory.getLogger(SocketFactory.class);

    private DestKey destKey;

    public SocketFactory(DestKey destKey) {
        this.destKey = destKey;
    }

    @Override
    public Socket create() {
        try {
            return new Socket(destKey.getAddress(), destKey.getPort());
        } catch (IOException e) {
            logger.error("Failed to initialize a socket resource: {}", destKey);
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy(Socket resource) {
        try {
            resource.close();
        } catch (IOException e) {
            logger.error("Failed to destroy the socket resource: {}", resource);
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isValid(Socket resource) {
        return !resource.isClosed();
    }

    @Override
    public Class<Socket> getType() {
        return Socket.class;
    }
}
