package com.npf.cloud.boot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: 宁培峰
 * @Description:异步执行线程池配置
 * @Date: Created in 18:44 2018/7/25 0025
 * @Modified By
 */
@ConfigurationProperties(prefix = "s1.spring.async")
public class AsyncThreadPoolProperties {

    //核心线程数量，永存数量
    private int corePoolSize=4;

    //最大线程数量
    private int maxPoolSize=100;

    //空闲线程存活时间秒
    private int keepAliveSeconds=60;

    //执行队列
    private int queueCapacity=100;


    public AsyncThreadPoolProperties(){}




    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }
}
