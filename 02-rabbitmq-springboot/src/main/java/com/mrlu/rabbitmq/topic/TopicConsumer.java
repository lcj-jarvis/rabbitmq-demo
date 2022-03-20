package com.mrlu.rabbitmq.topic;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author 简单de快乐
 * @date 2021-05-21 21:53
 */
@Component
public class TopicConsumer {

    /**
     * 去容器中获取到注册的队列
     */
    @RabbitListener(queues = "02-rabbitmq-springboot-topic-firstQueue")
    public void receive1(Map<String, Object> map){
        System.out.println("消费者1："+ map.toString());
    }

    @RabbitListener(queues = "02-rabbitmq-springboot-topic-secondQueue")
    public void receive2(Map<String, Object> map){
        System.out.println("消费者2："+ map.toString());
    }

    @RabbitListener(queues = "02-rabbitmq-springboot-topic-thirdQueue")
    public void receive3(Map<String, Object> map){
        System.out.println("消费者3："+ map.toString());
    }
}
