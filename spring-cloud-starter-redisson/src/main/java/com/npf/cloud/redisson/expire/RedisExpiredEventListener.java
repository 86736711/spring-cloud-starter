package com.npf.cloud.redisson.expire;

import com.npf.cloud.redisson.RedisConfig;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.map.event.EntryEvent;
import org.redisson.api.map.event.EntryExpiredListener;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.redisson.expire
 * @ClassName: RedisExpiredEventListener
 * @Author: ningpf
 * @Description: 过期key的监听器
 * @Date: 2020/3/15 15:30
 * @Version: 1.0
 */
@Slf4j
@Component
public class RedisExpiredEventListener implements EntryExpiredListener<String,Object> {


    /**
     *
     * @param event
     */
    @Override
    public void onExpired(EntryEvent<String,Object> event) {
        Set<RedisKeyExpireProcessor> processors = RedisConfig.getProcessor(event.getSource().getName().trim());
        for (RedisKeyExpireProcessor processor:processors) {
            processor.process(event.getKey(),event.getValue());
        }
    }
}
