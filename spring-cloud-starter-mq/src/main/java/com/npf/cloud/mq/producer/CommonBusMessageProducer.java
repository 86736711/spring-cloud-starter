package com.npf.cloud.mq.producer;


import com.npf.cloud.mq.producer.constant.RabbitMqConstant;

import java.io.Serializable;
/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.mq.config
 * @ClassName: CommonBusMessageProducer
 * @Author: ningpf
 * @Description: 消息生产者，通用交换机
 * @Date: 2020/3/15 16:48
 * @Version: 1.0
 */
public abstract class CommonBusMessageProducer<T extends Serializable> extends AbstractRabbitMessageProducer<T> {

    @Override
    public final String exchange() {
        return RabbitMqConstant.Exchange.EXCHANGE_COMMON;
    }

}
