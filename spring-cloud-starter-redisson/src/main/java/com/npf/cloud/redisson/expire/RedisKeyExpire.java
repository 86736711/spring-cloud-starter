package com.npf.cloud.redisson.expire;

import java.lang.annotation.*;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.redisson.expire
 * @ClassName: RedisKeyExpire
 * @Author: ningpf
 * @Description: 过期key的处理
 * @Date: 2020/3/15 15:31
 * @Version: 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisKeyExpire {

    /**
     * 主题（map名称）
     * @return
     */
    String[] topics();

}
