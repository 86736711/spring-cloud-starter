package com.npf.cloud.boot.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: 宁培峰
 * @Description:
 * @Date: Created in 18:55 2018/7/25 0025
 * @Modified By
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({AsyncThreadPoolProperties.class})
@EnableAsync(proxyTargetClass = true)
public class AsyncThreadPoolConfigration implements AsyncConfigurer{

    @Autowired
    AsyncThreadPoolProperties asyncThreadPoolProperties;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(asyncThreadPoolProperties.getCorePoolSize());
        executor.setMaxPoolSize(asyncThreadPoolProperties.getMaxPoolSize());
        executor.setKeepAliveSeconds(asyncThreadPoolProperties.getKeepAliveSeconds());
        executor.setQueueCapacity(asyncThreadPoolProperties.getQueueCapacity());
        //替换默认线程池,线程队列满了以后交给调用者执行,也就是同步执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("s1-async-task");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
                log.error("Exception message - " + throwable.toString());
                for (StackTraceElement traceElement : throwable.getStackTrace())
                log.error("\tat " + traceElement);
                for (Object param : objects) {
                    log.error("Parameter value - " + param);
                }
            }
        };
    }



    @Bean
    public ForkJoinPool forkJoinPool(){
        return new ForkJoinPool
                (30,
                        ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                        null, true);
    }


}
