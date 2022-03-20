package com.mrlu.rabbbitmq.fanout;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 简单de快乐
 * @date 2021-05-21 17:22
 */
@Component
public class FindOutModelConsumer {

    @RabbitListener(bindings = {
            //绑定交换机和队列
            @QueueBinding(
                    //创建临时队列
                    value = @Queue,
                    //设置交换机以及交换机的类型
                    exchange = @Exchange(value = "01-rabbitmq-springboot-fanout-exchange",
                            type = "fanout")
            )
    })
    public void receive1(String message){
        System.out.println("消费者1==》"+message);
    }

    @RabbitListener(bindings = {
            //绑定交换机和队列
            @QueueBinding(
                    //创建临时队列
                    value = @Queue,
                    //设置交换机以及交换机的类型
                    exchange = @Exchange(value = "01-rabbitmq-springboot-fanout-exchange",
                            type = "fanout")
            )
    })
    public void receive2(String message){
        System.out.println("消费者2==》"+message);
    }
}
