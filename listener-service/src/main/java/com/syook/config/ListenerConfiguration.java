package com.syook.config;

import com.syook.listener.TcpServer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class ListenerConfiguration {

    // Create an Executor just to start the listener on a different thread
    @Bean("tcpAcceptExecutor")
    public Executor acceptExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    // Declare thread pool while listening socket message.
    @Bean("tcpWorkerExecutor")
    public ThreadPoolTaskExecutor workerExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(25);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("tcp-worker-");
        executor.initialize();
        return executor;
    }

}
