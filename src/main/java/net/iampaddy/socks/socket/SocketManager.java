package net.iampaddy.socks.socket;

import net.iampaddy.pool.DefaultResourcePool;
import net.iampaddy.pool.ResourcePool;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description Here
 *
 * @author paddy.xie
 */
public final class SocketManager {

    private final static SocketManager manager = new SocketManager();
    private Map<DestKey, ResourcePool<Socket>> sockets;
    private ReentrantLock lock;

    private SocketManager() {
        sockets = new HashMap<>();
        lock = new ReentrantLock();
    }

    public static SocketManager getInstance() {
        return manager;
    }

    public Socket connect(DestKey key) {
        ResourcePool<Socket> pool = sockets.get(key);
        if(pool == null) {
            pool = new DefaultResourcePool<>(null, new SocketFactory(key));
            sockets.put(key, pool);
        }
        return (Socket)pool.get();
    }


}
