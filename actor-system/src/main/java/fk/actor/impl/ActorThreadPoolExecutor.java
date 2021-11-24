package fk.actor.impl;

import fk.actor.error.ResourceNotAvailable;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ActorThreadPoolExecutor extends ThreadPoolExecutor {

    public ActorThreadPoolExecutor(int maximumPoolSize, int bufferSize) {
        super(maximumPoolSize, maximumPoolSize, 60, SECONDS,
                new ArrayBlockingQueue<>(bufferSize),
                (r, executor) -> {
                    throw new ResourceNotAvailable("no threads are available for executing tasks");
                });
    }
}
