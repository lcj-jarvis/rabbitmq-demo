package com.mrlu.rabbbitmq.routing;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 简单de快乐
 * @date 2021-05-21 17:40
 */
@Component
public class RoutingModelConsumer {

    @RabbitListener(bindings = {
            //队列绑定交换机
            @QueueBinding(
                    //临时队列
                    value = @Queue,
                    //设置交换机以及交换机的类型
                    exchange = @Exchange(value = "01-rabbitmq-springboot-routing-direct-exchange",
                    type = "direct"),
                    //设置路由的key
                    key = {
                            "info",
                            "error",
                            "warn"
                    }
            )
    })
    public void receive1(String message){
        System.out.println("消费者1"+message);
    }

    @RabbitListener(bindings = {
            //队列绑定交换机
            @QueueBinding(
                    //临时队列
                    value = @Queue,
                    //设置交换机以及交换机的类型
                    exchange = @Exchange(value = "01-rabbitmq-springboot-routing-direct-exchange",
                            type = "direct"),
                    //设置路由的key
                    key = {
                            "error",
                    }
            )
    })
    public void receive2(String message){
        System.out.println("消费者2"+message);
    }
}
