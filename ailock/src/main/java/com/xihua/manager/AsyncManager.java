package com.xihua.manager;

import com.xihua.utils.SpringUtils;
import com.xihua.utils.ThreadUtil;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AsyncManager {

    private ScheduledExecutorService executor = SpringUtils.getBean("scheduledExecutorService");
    private static AsyncManager asyncManager = new AsyncManager();

    public AsyncManager() {
    }

    public static AsyncManager asyncManager() {
        return asyncManager;
    }

    public void execute(TimerTask task) {
        this.executor.schedule(task, 10L, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        ThreadUtil.shutdownAndAwaitTermination(this.executor);
    }
}
