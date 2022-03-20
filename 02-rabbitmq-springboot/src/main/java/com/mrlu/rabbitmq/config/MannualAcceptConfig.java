package com.mrlu.rabbitmq.config;

import com.mrlu.rabbitmq.auto.MyAckReceiver;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 简单de快乐
 * @date 2021-05-22 14:39
 *
 *
 */

//@Configuration
public class MannualAcceptConfig {

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    private MyAckReceiver myAckReceiver;

    //配置手动确认，和手动确认的消费者myAckReceiver
    //@Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(cachingConnectionFactory);
        //相当于这个连个配置
        //#并发消费者初始化值
        //#spring.rabbitmq.listener.simple.concurrency=1
        //#并发消费者的最大值
        //#spring.rabbitmq.listener.simple.max-concurrency=10
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(10);
        //RabbitMq默认是自动确认，这里改成手动确认
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);

        //设置队列,前提队列是已经存在的
        container.setQueueNames("02-rabbitmq-springboot-fanout-firstQueue",
                "02-rabbitmq-springboot-fanout-secondQueue");

        //其他设置队列的方法
        //container.setQueues(new Queue("02-rabbitmq-springboot-mannulAdd-firstQueue"));
        //container.addQueues(new Queue("02-rabbitmq-springboot-mannulAdd-firstQueue"));

        //设置消息的监听器。
        container.setMessageListener(myAckReceiver);
        return container;
    }
}
