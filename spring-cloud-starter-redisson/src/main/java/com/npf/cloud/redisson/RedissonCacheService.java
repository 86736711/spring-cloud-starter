package com.npf.cloud.redisson;

import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ProjectName: spring-cloud-starter-redisson
 * @Package: com.npf.cloud.redisson.queue
 * @ClassName: RedissonCacheService
 * @Author: ningpf
 * @Description: redisson缓存管理器
 * @Date: 2020/3/15 15:34
 * @Version: 1.0
 */
@Configuration
@EnableCaching
public class RedissonCacheService {

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        return new RedissonSpringCacheManager(redissonClient);
    }
}
