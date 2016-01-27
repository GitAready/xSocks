package net.iampaddy.socks.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Paddy on 2016/1/26.
 */
public final class DataForwarder {

    private static final DataForwarder forwarder = new DataForwarder();
    private Lock lock;
    private Map<DestKey, ChannelHandlerContext> serverMap;
    private Map<DestKey, SocketChannel> clientMap;

    private DataForwarder() {
        lock = new ReentrantLock(true);
        serverMap = new HashMap<>();
        clientMap = new HashMap<>();
    }

    public static DataForwarder getInstance() {
        return forwarder;
    }

    public boolean register(DestKey destKey, ChannelHandlerContext ctx, SocketChannel channel) {
        lock.lock();
        serverMap.put(destKey, ctx);
        clientMap.put(destKey, channel);
        lock.unlock();
        return true;
    }

    public void unregister(DestKey destKey) {
        lock.lock();
        serverMap.remove(destKey);
        clientMap.remove(destKey);
        lock.unlock();
    }

    public void sendToServer(DestKey destKey, ByteBuffer buffer) {

    }

    public void sendToClient(DestKey destKey, ByteBuf buffer) {

    }

}
