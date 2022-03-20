package com.mrlu.rabbitmq.config.publishconfirm;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 简单de快乐
 * @date 2021-07-09 22:05
 *
 * BackupConfig选一个配置
 *
 * 要在application.properties的配置文件中加入以下内容
 * spring.rabbitmq.publisher-confirm-type=correlated
 * #发送回调
 * spring.rabbitmq.publisher-returns=true
 * #通过设置 mandatory 参数可以在当消息传递过程中不可达目的地时将消息返回给生产者。
 * spring.rabbitmq.template.mandatory=true
 */
//@Configuration
public class PublishConfirmConfig {

    /**
     * 发布确认交换机
     */
    public static final String CONFIRM_EXCHANGE = "confirm_exchange";

    /**
     * 发布确认队列
     */
    public static final String CONFIRM_QUEUE = "confirm_queue";

    /**
     * routingKey
     */
    public static final String CONFIRM_ROUTING_KEY = "confirm_routing_key";

    @Bean
    public DirectExchange confirmExchange() {
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE).build();
    }

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE).build();
    }

    @Bean
    public Binding confirmQueueBindingConfirmExchange() {
        return BindingBuilder.bind(confirmQueue()).to(confirmExchange()).with(CONFIRM_ROUTING_KEY);
    }
}
