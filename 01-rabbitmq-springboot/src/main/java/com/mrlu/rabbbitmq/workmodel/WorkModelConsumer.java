package com.mrlu.rabbbitmq.workmodel;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author 简单de快乐
 * @date 2021-05-21 16:47
 *
 * @RabbitListener注解也可以用在方法上， springboot默认对rabbitmq的work模型使用的是平均发布的方式
 */
//@Component
public class WorkModelConsumer {

    /**
     * 消费者1
     */
    @RabbitListener(queuesToDeclare = @Queue(value = "01-rabbitmq-springboot-workmodel",durable = "true"))
    public void receive1(String message) throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        System.out.println("消费者1："+message);
    }

    /**
     * 消费者2
     * @param message
     */
    @RabbitListener(queuesToDeclare = @Queue(value = "01-rabbitmq-springboot-workmodel",durable = "true"))
    public void receive2(String message){
        System.out.println("消费者2："+message);
    }
}
