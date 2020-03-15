package com.npf.cloud.redisson.subscribe;

import java.lang.annotation.*;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.redisson.subscribe
 * @ClassName: RedisSubscribe
 * @Author: ningpf
 * @Description: 订阅者注解，这个订阅者需要实现MessageListener接口
 * @Date: 2020/3/15 15:35
 * @Version: 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisSubscribe {

    /**
     * 主题（map名称）
     * @return
     */
    String[] topics();
}
