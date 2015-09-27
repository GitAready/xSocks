package net.iampaddy.socks.socket;

import net.iampaddy.pool.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

/**
 * Description Here
 *
 * @author paddy.xie
 */
public class SocketResource implements Resource<Socket> {

    private Logger logger = LoggerFactory.getLogger(SocketResource.class);

    private DestKey destKey;
    private Socket socket;

    public SocketResource(DestKey destKey) {
        this.destKey = destKey;
    }

    @Override
    public void init() {
        try {
            socket = new Socket(destKey.getAddress(), destKey.getPort());

        } catch (IOException e) {
            logger.error("Initialize socket resource failed: ", e);
        }
    }

    @Override
    public void destroy() {
        try {
            socket.close();
        } catch (IOException e) {
            logger.error("Destroy socket resource failed: ", e);
        }
    }

    @Override
    public Socket get() {
        return socket;
    }

    @Override
    public boolean isValid() {
        return socket != null && !socket.isClosed();
    }
}
