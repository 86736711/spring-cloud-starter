package com.npf.cloud.redisson.queue;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.redisson.queue
 * @ClassName: RedisQueueProcessor
 * @Author: ningpf
 * @Description: 队列处理接口
 * @Date: 2020/3/15 15:34
 * @Version: 1.0
 */
public interface RedisQueueProcessor<T> {

    /**
     * 功能描述：处理方法
     *
     * @param value 内容
     * @return
     */
    void process(T value);
}
