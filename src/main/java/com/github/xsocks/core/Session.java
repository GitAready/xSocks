package com.github.xsocks.core;

import com.github.xsocks.socket.DestKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by pxie on 1/28/16.
 */
public class Session {

    private Logger logger = LoggerFactory.getLogger(Session.class);

    private String sessionId;
    private long createdOn;
    private long lastAccess;

    private DestKey destKey;

    protected Session(String sessionId, DestKey destKey) {
        this.sessionId = sessionId;
        this.createdOn = System.currentTimeMillis();
        this.lastAccess = System.currentTimeMillis();
        this.destKey = destKey;
    }

    public String getSessionId() {
        return sessionId;
    }

    public long getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(long lastAccess) {
        this.lastAccess = lastAccess;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public DestKey getDestKey() {
        return destKey;
    }

    public void setDestKey(DestKey destKey) {
        this.destKey = destKey;
    }

    public String toString() {
        return "Destination: " + destKey + "    Last Access: " + new Date(lastAccess);
    }
}
