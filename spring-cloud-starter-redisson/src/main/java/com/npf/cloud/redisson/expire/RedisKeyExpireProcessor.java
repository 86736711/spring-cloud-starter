package com.npf.cloud.redisson.expire;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.redisson.expire
 * @ClassName: RedisKeyExpireProcessor
 * @Author: ningpf
 * @Description: key过期的处理
 * @Date: 2020/3/15 15:31
 * @Version: 1.0
 */
public interface RedisKeyExpireProcessor {

    /**
     * 功能描述：redis键过期之后的处理
     *
     * @param key RMapCache对象中的key
     * @param value RMapCache对象中的key对应的value值
     */
    <T> void process(String key, T value);
}
