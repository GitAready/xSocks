package net.iampaddy.socks.socket;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Description Here
 *
 * @author paddy.xie
 */
public class AsyncSocketChannelFactory implements PooledObjectFactory<AsynchronousSocketChannel> {

    private Logger logger = LoggerFactory.getLogger(AsyncSocketChannelFactory.class);

    private DestKey destKey;

    private AsynchronousChannelGroup group;

    public AsyncSocketChannelFactory(DestKey destKey, AsynchronousChannelGroup group) {
        this.destKey = destKey;
        this.group = group;
    }

    @Override
    public PooledObject<AsynchronousSocketChannel> makeObject() throws Exception {
        try {
            logger.debug("{} - Creating new socket", destKey);

            AsynchronousSocketChannel channel = AsynchronousSocketChannel.open(group);
            channel.connect(new InetSocketAddress(destKey.getAddress(), destKey.getPort())).get();

            PooledObject<AsynchronousSocketChannel> o = new PooledChannel(destKey, channel);
            return o;
        } catch (IOException e) {
            logger.error("{} - Failed to create a socket", destKey);
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroyObject(PooledObject<AsynchronousSocketChannel> p) throws Exception {
        PooledChannel pooledChannel = (PooledChannel) p;
        try {
            AsynchronousSocketChannel channel = p.getObject();
            channel.close();
            logger.debug("{} - Destroyed socket successfully", pooledChannel.getDestKey());
        } catch (IOException e) {
            logger.error("{} - Failed to destroy the socket", pooledChannel.getDestKey());
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validateObject(PooledObject<AsynchronousSocketChannel> p) {
        return p.getObject().isOpen();
    }

    @Override
    public void activateObject(PooledObject<AsynchronousSocketChannel> p) throws Exception {
        //System.out.println("Activate Object " + p);
    }

    @Override
    public void passivateObject(PooledObject<AsynchronousSocketChannel> p) throws Exception {
        //System.out.println("Passivate Object " + p);
    }
}
