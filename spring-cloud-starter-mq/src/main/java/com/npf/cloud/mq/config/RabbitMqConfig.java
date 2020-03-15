package com.npf.cloud.mq.config;

import com.npf.cloud.mq.producer.constant.RabbitMqConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.mq.config
 * @ClassName: RabbitMqConfig
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2020/3/15 16:48
 * @Version: 1.0
 */
@Configuration
@EnableRabbit
public class RabbitMqConfig {

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }


    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactoryAck(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //开启手动 ack
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }

    @Bean
    public TopicExchange initExchange() {
        return new TopicExchange(RabbitMqConstant.Exchange.EXCHANGE_COMMON);
    }

    /**
     * 功能描述：店铺信息的消息队列
     */
    @Bean(name = "mq_queue_base_shop")
    public Queue queueMessageBaseShop() {
        return new Queue(RabbitMqConstant.Queue.QUEUE_BASE);
    }


    /**
     * 功能描述：店铺信息的消息队列-绑定交换机
     */
    @Bean
    public Binding bindingExchangeMessageBaseShop(@Qualifier("mq_queue_base_shop") Queue queueMessageNotify, @Qualifier("initExchange")TopicExchange exchange) {
        return BindingBuilder.bind(queueMessageNotify).to(exchange).with(RabbitMqConstant.Queue.QUEUE_BASE + ".#");
    }

    
}
