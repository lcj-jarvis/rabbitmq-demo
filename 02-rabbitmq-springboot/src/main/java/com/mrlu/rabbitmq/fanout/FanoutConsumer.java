package com.mrlu.rabbitmq.fanout;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 简单de快乐
 * @date 2021-05-21 23:01
 *
 *  这种方式一个队列也是可以支持多个消费者的
 */
//@Component
public class FanoutConsumer {

    @RabbitListener(queues = "02-rabbitmq-springboot-fanout-firstQueue")
    public void receive1(Map<String, Object> map) throws InterruptedException {
        System.out.println("消费者1："+map.toString());
    }

   // @RabbitListener(queues = "02-rabbitmq-springboot-fanout-firstQueue")
    @RabbitListener(queues = "02-rabbitmq-springboot-fanout-secondQueue")
    public void receive2(Map<String, Object> map) throws InterruptedException {
        System.out.println("消费者2："+map.toString());
    }

    //@RabbitListener(queues = "02-rabbitmq-springboot-fanout-firstQueue")
    @RabbitListener(queues = "02-rabbitmq-springboot-fanout-thirdQueue")
    public void receive3(Map<String, Object> map) throws InterruptedException {
        System.out.println("消费者3："+map.toString());
    }

}
