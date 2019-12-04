package com.xihua.config;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: 线程池配置类
 * @author: admin
 */
@Configuration
public class ThreadPoolConfig {
    private int corePoolSize = 5;
    private int maxPoolSize = 20;
    private int queueCapacity = 50;
    private int keepAliveSeconds = 300;

    public ThreadPoolConfig() {
    }

    @Bean(name = {"threadPoolTaskExecutor"})
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(this.maxPoolSize);
        executor.setCorePoolSize(this.corePoolSize);
        executor.setQueueCapacity(this.queueCapacity);
        executor.setKeepAliveSeconds(this.keepAliveSeconds);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    @Bean(name = {"scheduledExecutorService"})
    protected ScheduledExecutorService scheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(this.corePoolSize, (new BasicThreadFactory.Builder()).namingPattern("schedule-pool-%d").daemon(true).build());
    }
}
