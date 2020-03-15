package com.npf.cloud.mq.producer.constant;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.mq.producer.constant
 * @ClassName: RabbitMqConstant
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2020/3/15 16:51
 * @Version: 1.0
 */
public class RabbitMqConstant {
    public static class Exchange{
        /**
         * 交换机：通用总线
         */
        public static final String EXCHANGE_COMMON = "cloud-common-bus";

    }

    public static class Queue{


        /**
         * 基础的事件队列
         */
        public static final String QUEUE_BASE = "cloud.base.queue";


    }
}
