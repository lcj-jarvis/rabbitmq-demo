package com.mrlu.rabbbitmq.helloworld;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 简单de快乐
 * @date 2021-05-21 16:14
 *
 *
 * @RabbitListener 表示被该注解标注的类为消费者。queuesToDeclare属性表示定义一个队列。
 *                 该注解可以使用在类和方法上面
 * @Queue 注解用来完成对一个队列的定义，@Queue的value值表示队列的名称。
 *        要和生成者convertAndSend方法的routingkey参数一致
 */
//@Component
//@RabbitListener(queuesToDeclare = @Queue(value = "hello",durable = "true",autoDelete = "false"))
public class RabbitMqConsumer {

    @RabbitHandler
    public void receive(String message){
        System.out.println("message====>"+message);
    }
}
