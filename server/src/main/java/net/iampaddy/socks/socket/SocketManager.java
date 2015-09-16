package net.iampaddy.socks.socket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xpjsk on 2015/9/14.
 */
public final class SocketManager {

    private Map<DestKey, SocketPool> sockets;
    private ReentrantLock lock;

    private final static SocketManager manager = new SocketManager();
    public static SocketManager getInstance() {
        return manager;
    }

    private SocketManager() {
        sockets = new HashMap<>();
        lock = new ReentrantLock();
    }

    public Socket connect(DestKey destKey) {
        SocketPool pool = sockets.get(destKey);
        if (pool == null) {
            try {
                lock.lock();
                pool = sockets.get(destKey);
                if (pool == null) {
                    pool = new SocketPool(destKey);
                    pool.init();
                    sockets.put(destKey, pool);
                }
            } finally {
                lock.unlock();
            }
        }
        return pool.connect();
    }

}
