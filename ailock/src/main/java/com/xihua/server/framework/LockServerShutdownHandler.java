package com.xihua.server.framework;

import java.util.concurrent.CountDownLatch;


public class LockServerShutdownHandler {

    private final CountDownLatch shutdownLatch;

    public LockServerShutdownHandler(CountDownLatch shutdownLatch) {
        this.shutdownLatch = shutdownLatch;
    }

    void handle(ServerState state) {
        if (state == ServerState.ERROR || state == ServerState.SHUTDOWN) {
            shutdownLatch.countDown();
        }
    }
}
