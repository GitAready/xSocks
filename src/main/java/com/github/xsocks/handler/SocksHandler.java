package com.github.xsocks.handler;

import com.github.xsocks.Configuration;
import com.github.xsocks.socket.DestKey;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import com.github.xsocks.connector.Connector;
import com.github.xsocks.connector.ConnectorManager;

/**
 * Created by pxie on 1/29/16.
 */
public abstract class SocksHandler extends ChannelInboundHandlerAdapter {

    private static final AttributeKey<Configuration> CONF = AttributeKey.newInstance("configuration");
    private static final AttributeKey<ConnectorManager> CONN = AttributeKey.newInstance("connectorManager");

    protected Connector selectConnector(DestKey destKey, ChannelHandlerContext context) {
        return getConnectorManager(context).select(destKey);
    }

    protected Configuration getConfiguration(ChannelHandlerContext ctx) {
        return ctx.attr(CONF).get();
    }

    protected ConnectorManager getConnectorManager(ChannelHandlerContext ctx) {
        return ctx.attr(CONN).get();
    }
}
