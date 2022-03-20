package com.mrlu.rabbitmq.config.workqueue;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 简单de快乐
 * @date 2021-07-11 15:26
 */
@Configuration
public class WorkQueueConfig {

    public static final String MANUAL_QUEUE = "first_manual_queue";

    @Bean
    public Queue firstManualQueue() {
        //设置为惰性队列
        //return QueueBuilder.durable(MANUAL_QUEUE).lazy().build();

        return QueueBuilder.durable(MANUAL_QUEUE).build();
    }
}
