package com.mrlu.rabbitmq.config.delayqueue;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 简单de快乐
 * @date 2021-07-08 22:41
 *
 * 延迟队列的配置
 *
 * 参考延时队列实战图进行配置
 */
@Configuration
public class DelayQueueConfig {

    /**
     * 队列
     */
    public static final String DELAY_QUEUE_NAME = "delayed.queue";
    /**
     * 交换机
     */
    public static final String DELAY_EXCHANGE_NAME = "delayed.exchange";

    /**
     * routingKey
     */
    public static final String DELAY_ROUTING_KEY = "delayed.routingKey";

    /**
     * 声明交换机，因为这个交换机类型是插件自定义的，所以使用自定义的交换机
     */
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> arguments = new HashMap<>(16);
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange(DELAY_EXCHANGE_NAME, "x-delayed-message",
                true, false, arguments);
    }

    /**
     * 声明队列
     * @return
     */
    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAY_QUEUE_NAME);
    }

    /**
     * 队列绑定交换机
     */
    @Bean
    public Binding binding(Queue delayedQueue, CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange)
                .with(DELAY_ROUTING_KEY).noargs();
    }
}
