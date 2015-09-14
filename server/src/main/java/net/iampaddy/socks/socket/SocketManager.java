package net.iampaddy.socks.socket;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xpjsk on 2015/9/14.
 */
public class SocketManager {

    private Map<DestKey, SocketPool> sockets;

    public SocketManager() {
        sockets = new HashMap<>();
    }

    public Socket getSocket(DestKey destKey) {
        SocketPool pool = sockets.get(destKey);
        if(pool == null) {
            pool = new SocketPool(destKey);
            pool.init();
            sockets.put(destKey, pool);
        }
        return pool.getOne();
    }

}
