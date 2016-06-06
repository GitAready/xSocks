package com.github.xsocks.core;

import com.github.xsocks.NamedThreadFactory;
import com.github.xsocks.event.Event;
import com.github.xsocks.event.EventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Created by pxie on 2/6/16.
 */
public class EventDispatcher {

    private static EventDispatcher instance = new EventDispatcher();

    private Logger logger = LoggerFactory.getLogger(EventDispatcher.class);

    private ConcurrentMap<Class<? extends Event>, EventProcessor> processorMap;

    private ExecutorService executor;

    public EventDispatcher() {
        processorMap = new ConcurrentHashMap<>();
    }

    public <E extends Event> boolean register(Class<E> clazz, EventProcessor<E> processor) {
        return processorMap.putIfAbsent(clazz, processor) == null;
    }

    public EventProcessor deregister(Class<? extends Event> clazz) {
        return processorMap.remove(clazz);
    }

    public <S> void dispatch(Event<S> event) {
        if (executor != null && !executor.isShutdown()) {

            EventProcessor<Event<S>> processor = processorMap.get(event.getClass());

            try {
                executor.submit(() -> {
                    try {
                        processor.process(event);
                    } catch (Throwable t) {
                        processor.failure(event, t);
                    }
                });

                return;
            } catch (RejectedExecutionException e) {
                processor.failure(event, e);
            } catch (Throwable t) {
                logger.error(t.getMessage(), t);
                throw new RuntimeException(t);
            }
        }

        throw new RuntimeException("Dispatcher " + (executor == null ? "not started" : "is shutdown"));
    }

    public void start() {
        executor = Executors.newCachedThreadPool(new NamedThreadFactory("Event"));
    }

    public static EventDispatcher getInstance() {
        return instance;
    }
}
