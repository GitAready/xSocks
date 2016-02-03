package com.github.xsocks.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.CompletionHandler;

/**
 * Created by pxie on 2/3/16.
 */
public class ForwardCallback implements CompletionHandler<Integer, Session> {

    private Logger logger = LoggerFactory.getLogger(ForwardCallback.class);

    private DataForwarder dataForwarder;

    public ForwardCallback(DataForwarder dataForwarder) {
        this.dataForwarder = dataForwarder;
    }

    @Override
    public void completed(Integer result, Session session) {

        logger.debug("{} - Forwarded {} bytes", session.getSessionId(), result);

        // TODO network traffic count
    }

    @Override
    public void failed(Throwable exc, Session session) {
        dataForwarder.broken(session);
    }
}
