package com.github.xsocks.event;

import com.github.xsocks.NamedThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class EventMonster {

    ExecutorService executor = Executors.newCachedThreadPool(new NamedThreadFactory("Monster"));

    public Toy play() {
        return null;
    }

    public void bye(Toy toy) {
        if (toy == null) {
            throw new RuntimeException("Monster is yelling you to return the toy!!!");
        }


    }
}
