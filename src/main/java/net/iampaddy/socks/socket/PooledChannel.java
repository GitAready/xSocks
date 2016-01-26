package net.iampaddy.socks.socket;

import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.net.Socket;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Description here
 *
 * @author paddy.xie
 */
public class PooledChannel extends DefaultPooledObject<AsynchronousSocketChannel> {

    private DestKey destKey;

    /**
     * Create a new instance that wraps the provided object so that the pool can
     * track the state of the pooled object.
     *
     * @param channel The object to wrap
     */
    public PooledChannel(DestKey destKey, AsynchronousSocketChannel channel) {
        super(channel);
        this.destKey = destKey;
    }


    public DestKey getDestKey() {
        return this.getDestKey();
    }

}
