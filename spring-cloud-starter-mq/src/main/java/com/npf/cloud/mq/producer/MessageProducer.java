package com.npf.cloud.mq.producer;

import java.io.Serializable;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.mq.config
 * @ClassName: MessageProducer
 * @Author: ningpf
 * @Description: 消息生产者接口
 * @Date: 2020/3/15 16:48
 * @Version: 1.0
 */
public interface MessageProducer<T extends Serializable> {


    void produce(T t);
}
