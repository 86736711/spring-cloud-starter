package com.npf.cloud.mq.producer;

import com.npf.cloud.framework.util.JSONUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.mq.config
 * @ClassName: AbstractRabbitMessageProducer
 * @Author: ningpf
 * @Description: 消息生产者
 * @Date: 2020/3/15 16:48
 * @Version: 1.0
 */
public abstract class AbstractRabbitMessageProducer<T extends Serializable> implements MessageProducer<T> {

    @Autowired
    protected AmqpTemplate rabbitTemplate;

    @Override
    public void produce(T t) {
        this.rabbitTemplate.convertAndSend(exchange(),queue(), JSONUtil.toStr(t));
    }

    public abstract String exchange();

    public abstract String queue();


}
