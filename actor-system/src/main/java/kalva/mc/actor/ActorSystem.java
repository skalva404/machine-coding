package kalva.mc.actor;


import kalva.mc.actor.impl.ActorThreadPoolExecutor;
import kalva.mc.actor.impl.SimpleActorService;

import java.util.concurrent.ExecutorService;

public class ActorSystem implements LifeCycle {

    static Config config;
    private static ActorSystem ourInstance;

    static {
        config = new Config();
        ourInstance = new ActorSystem();
    }

    private static ActorSystem getInstance() {
        return ourInstance;
    }

    private ActorSystem() {
        init();
    }

    private ActorThreadPoolExecutor executor;
    private ActorService actorService = new SimpleActorService(config);

    public static ActorService actorService() {
        return getInstance().actorService;
    }

    public static ExecutorService cpu() {
        return getInstance().executor;
    }

    @Override
    public void init() {
        executor = new ActorThreadPoolExecutor(config.threadPoolSize(), config.mailBoxSize());
        executor.prestartAllCoreThreads();
        actorService.init();
    }

    @Override
    public void close() throws Exception {
        actorService.close();
    }
}
