package net.iampaddy.socks.socket;

import java.util.Date;

/**
 * Created by xpjsk on 2015/9/14.
 */
public class Socket {

    private SocketPool pool;
    private java.net.Socket socket;

    private boolean valid;
    private Date createTime;
    private Date lastAccess;
    private int count;

    public Socket(java.net.Socket socket, SocketPool pool) {
        this.socket = socket;
        this.pool = pool;



        this.valid = true;
        this.createTime = new Date();
        this.lastAccess = null;
        this.count = 0;
    }

    public java.net.Socket getSocket() {
        return socket;
    }

    public boolean isValid() {
        return valid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public int getCount() {
        return count;
    }

}
