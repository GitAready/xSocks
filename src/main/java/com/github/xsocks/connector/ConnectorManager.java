package com.github.xsocks.connector;

import com.github.xsocks.Configuration;
import com.github.xsocks.socket.DestKey;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Description here
 *
 * @author paddy.xie
 */
public class ConnectorManager {

    private ConcurrentMap<DestKey, Connector> cache;
    private Configuration conf;

    public ConnectorManager(Configuration conf) {
        this.conf = conf;
        this.cache = new ConcurrentHashMap<>();
    }


    //TODO DNS check
    public Connector select(DestKey destKey) {

        if(conf.getConnector().isEmpty()) {
            throw new RuntimeException("No connector configured, please check your configuration file");
        }

        if(conf.getConnector().size() == 1) {
            return conf.getConnector().get(0);
        }

        if(cache.containsKey(destKey)) {
            return cache.get(destKey);
        }

        for (Connector connector : conf.getConnector()) {
            try {
                if (connector.resolve(destKey)) {
                    cache.putIfAbsent(destKey, connector);
                    return connector;
                }
            } catch (Exception e) {
                // ignore
            }
        }

        return null;
    }

}
