package com.mrlu.rabbitmq.config.priorityqueue;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 简单de快乐
 * @date 2021-07-09 23:39
 *
 * 优先级队列：会对队列中的消息进行优先级的排序，优先级高的消息先发送给消费者。
 *           优先级的范围是0-255
 *
 * 要让队列实现优先级需要做的事情有如下事情:
 * （1）队列需要设置为优先级队列，
 * （2）消息需要设置消息的优先级
 * （3）消费者需要等待消息已经发送到队列中才去消费因为，这样才有机会对消息进行排序
 *     即先运行生产者，后运行消费者。
 */
@Configuration
public class PriorityQueueConfig {

    /**
     * 发布确认交换机
     */
    public static final String PRIORITY_EXCHANGE = "priority_exchange";

    /**
     * 发布确认队列
     */
    public static final String PRIORITY_QUEUE = "priority_queue";

    /**
     * routingKey
     */
    public static final String PRIORITY_ROUTING_KEY = "priority_routing_key";


    @Bean
    public DirectExchange priorityExchange() {
        return ExchangeBuilder.directExchange(PRIORITY_EXCHANGE).build();
    }

    @Bean
    public Queue priorityQueue() {
        //maxPriority(10),设置队列的最大优先级为10
        return QueueBuilder.durable(PRIORITY_QUEUE).maxPriority(10).build();
    }

    @Bean
    public Binding priorityQueueBindingPriorityExchange() {
        return BindingBuilder.bind(priorityQueue()).to(priorityExchange()).with(PRIORITY_ROUTING_KEY);
    }

}
