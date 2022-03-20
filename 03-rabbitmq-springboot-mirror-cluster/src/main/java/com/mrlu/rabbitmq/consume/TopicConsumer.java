package com.mrlu.rabbitmq.consume;

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
    @RabbitListener(queues = "03-rabbitmq-springboot-mirror-cluster-firstQueue")
    public void receive1(Map<String, Object> map){
        System.out.println("消费者1："+ map.toString());
    }

    @RabbitListener(queues = "03-rabbitmq-springboot-mirror-cluster-secondQueue")
    public void receive2(Map<String, Object> map){
        System.out.println("消费者2："+ map.toString());
    }

    @RabbitListener(queues = "03-rabbitmq-springboot-mirror-cluster-thirdQueue")
    public void receive3(Map<String, Object> map){
        System.out.println("消费者3："+ map.toString());
    }
}
