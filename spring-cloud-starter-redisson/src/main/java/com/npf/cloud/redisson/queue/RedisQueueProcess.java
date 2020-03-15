package com.npf.cloud.redisson.queue;

import java.lang.annotation.*;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.redisson.queue
 * @ClassName: RedisQueueProcess
 * @Author: ningpf
 * @Description: 队列
 * @Date: 2020/3/15 15:34
 * @Version: 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisQueueProcess {

    /**
     * 主题（队列名称）
     *
     * @return
     */
    String[] topics();
}
