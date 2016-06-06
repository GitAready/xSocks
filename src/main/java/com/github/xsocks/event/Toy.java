package com.github.xsocks.event;

import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 */
public class Toy {

    private LinkedBlockingQueue queue;


    /**
     * Players use this method to send messages to monster
     * @param msg message send to monster
     */
    public void send(ByteBuffer msg) {

    }

    /**
     * Monster doesn't know the Toy has a bug, player can hack it so monster will do some additional things
     */
    public void hack() {

    }

    /**
     * Monster use this method to send messages to the player
     * @param msg message send to players
     */
    protected void reply(ByteBuffer msg) {

    }


}
