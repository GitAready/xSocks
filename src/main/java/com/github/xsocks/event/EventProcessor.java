package com.github.xsocks.event;

/**
 * Created by pxie on 2/6/16.
 */
public interface EventProcessor<E extends Event> {

    void process(E event);

    void failure(E event, Throwable t);

}
