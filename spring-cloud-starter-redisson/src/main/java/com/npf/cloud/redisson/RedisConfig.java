package com.npf.cloud.redisson;

import com.npf.cloud.redisson.expire.RedisKeyExpire;
import com.npf.cloud.redisson.expire.RedisKeyExpireProcessor;
import com.npf.cloud.redisson.queue.RedisQueueProcess;
import com.npf.cloud.redisson.queue.RedisQueueProcessor;
import com.npf.cloud.redisson.subscribe.RedisSubscribe;
import com.npf.cloud.redisson.util.RedissonUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ProjectName: spring-cloud-starter-redisson
 * @Package: com.npf.cloud.redisson.queue
 * @ClassName: RedisConfig
 * @Author: ningpf
 * @Description: redisson的事件监听配置，定义出订阅者模式的使用
 * @Date: 2020/3/15 15:34
 * @Version: 1.0
 */
@Slf4j
@Component
public class RedisConfig implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * map名称和处理类对应
     */
    private static Map<String, Set<RedisKeyExpireProcessor>> processorMap = new ConcurrentHashMap<>();

    private static Map<String, Set<RedisQueueProcessor>> queueProcessorMap = new ConcurrentHashMap<>();

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 功能描述：Spring初始化完成之后执行
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        scanAnnotation(event);
    }

    /**
     * 功能描述：扫描RedisKeyExpire注解，加入processorMap中
     *
     * @param event 应用启动完成事件对象
     */
    private void scanAnnotation(ContextRefreshedEvent event) {
        scanKeyExpireAnnotation(event);
        scanSubscribeAnnotation(event);
        scanQueueAnnotation(event);
    }

    /**
     * 功能描述：扫描键过期处理类
     *
     * @param event 应用启动完成事件对象
     */
    private void scanKeyExpireAnnotation(ContextRefreshedEvent event) {
        // 通过注解获取相关的类
        Map<String, Object> map = new HashMap();
        map = event.getApplicationContext().getBeansWithAnnotation(RedisKeyExpire.class);
        for (Map.Entry<String, Object> entrymap : map.entrySet()) {
            try {
                RedisKeyExpire redisKeyExpireProcessor = entrymap.getValue().getClass().getDeclaredAnnotation(RedisKeyExpire.class);
                String[] topics = redisKeyExpireProcessor.topics();
                for (String topic : topics) {
                    Set<RedisKeyExpireProcessor> redisKeyExpireProcessors = processorMap.getOrDefault(topic, new HashSet<>());
                    redisKeyExpireProcessors.add((RedisKeyExpireProcessor) entrymap.getValue());
                    processorMap.put(topic, redisKeyExpireProcessors);
                    RedissonUtils.keyExpireListener(topic, redissonClient, true);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private void scanQueueAnnotation(ContextRefreshedEvent event) {
        // 通过注解获取相关的类
        Map<String, Object> map = new HashMap();
        map = event.getApplicationContext().getBeansWithAnnotation(RedisQueueProcess.class);
        for (Map.Entry<String, Object> entrymap : map.entrySet()) {
            try {
                RedisQueueProcess redisQueueProcessor = entrymap.getValue().getClass().getDeclaredAnnotation(RedisQueueProcess.class);
                String[] topics = redisQueueProcessor.topics();
                for (String topic : topics) {
                    Set<RedisQueueProcessor> redisQueueProcessors = queueProcessorMap.getOrDefault(topic, new HashSet<>());
                    redisQueueProcessors.add((RedisQueueProcessor) entrymap.getValue());
                    queueProcessorMap.put(topic, redisQueueProcessors);

                    RedissonUtils.listenQueue(topic, 10, redissonClient);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 功能描述：扫描订阅方法
     *
     * @param event 应用启动完成事件对象
     */
    private void scanSubscribeAnnotation(ContextRefreshedEvent event) {
        Map<String, Object> map = event.getApplicationContext().getBeansWithAnnotation(RedisSubscribe.class);
        for (Map.Entry<String, Object> entryMap : map.entrySet()) {
            RedisSubscribe redisSubscribeProcessor = entryMap.getValue().getClass().getDeclaredAnnotation(RedisSubscribe.class);
            String[] topics = redisSubscribeProcessor.topics();
            for (String topic : topics) {
                RedissonUtils.subscription(topic, redissonClient, (MessageListener) entryMap.getValue());
            }
        }
    }

    /**
     * 功能描述：根据map名称获取处理类
     *
     * @param topic 主题名称
     */
    public static Set<RedisKeyExpireProcessor> getProcessor(String topic) {
        return processorMap.get(topic);
    }

    public static Set<RedisQueueProcessor> getQueueProcessor(String topic) {
        return queueProcessorMap.get(topic);
    }

}
