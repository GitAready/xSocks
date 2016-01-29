package com.github.xsocks.socket;

import com.github.xsocks.NamedThreadFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description Here
 *
 * @author paddy.xie
 */
public final class SocketManager {

    private final static SocketManager manager = new SocketManager();

    private Map<DestKey, GenericObjectPool<AsynchronousSocketChannel>> channels;
    private ReentrantLock lock;

    private AsynchronousChannelGroup group;

    private SocketManager() {
        channels = new ConcurrentHashMap<>();
        lock = new ReentrantLock();
        try {
            ExecutorService executor = Executors.newCachedThreadPool(new NamedThreadFactory("Forwarder"));
            group = AsynchronousChannelGroup.withCachedThreadPool(executor, 5);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static SocketManager getInstance() {
        return manager;
    }

    public AsynchronousSocketChannel connect(DestKey key) {
        GenericObjectPool<AsynchronousSocketChannel> pool = getPool(key);
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean disconnect(DestKey key, AsynchronousSocketChannel socket) {
        GenericObjectPool<AsynchronousSocketChannel> pool = getPool(key);
        try {
            pool.invalidateObject(socket);
        } catch (Exception e) {
            // ignore
        }
        return true;
    }

    public boolean checkConnectivity(DestKey destKey) {
        GenericObjectPool<AsynchronousSocketChannel> pool = getPool(destKey);
        try {
            AsynchronousSocketChannel channel = pool.borrowObject();
            pool.returnObject(channel);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private GenericObjectPool<AsynchronousSocketChannel> getPool(DestKey key) {
        GenericObjectPool<AsynchronousSocketChannel> pool = channels.get(key);
        if (pool == null) {
            try {
                lock.lock();
                if ((pool = channels.get(key)) == null) {
                    GenericObjectPoolConfig config = new GenericObjectPoolConfig();
                    config.setMaxTotal(6);
                    config.setMaxIdle(2);

                    pool = new GenericObjectPool<>(new AsyncSocketChannelFactory(key, group), config);
                    channels.put(key, pool);
                }
            } finally {
                lock.unlock();
            }
        }
        return pool;
    }

}
