package com.mrlu.rabbbitmq.topics;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 简单de快乐
 * @date 2021-05-21 17:57
 */
@Component
public class TopicModelConsumer {

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(value = "01-hello-springboot-topic-exchange",type = "topic"),
                    //使用通配符匹配route的key
                    key = {"*.order.#"}
            )
    })
    public void receive1(String message){
        System.out.println("消费者1 *.order.# ==》"+message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(value = "01-hello-springboot-topic-exchange",type = "topic"),
                    //使用通配符匹配route的key
                    key = {"*.order.*"}
            )
    })
    public void receive2(String message){
        System.out.println("消费者2  *.order.* ==》"+message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(value = "01-hello-springboot-topic-exchange",type = "topic"),
                    //使用通配符匹配route的key
                    key = {"manager.*"}
            )
    })
    public void receive3(String message){
        System.out.println("消费者3  manager.* ==》"+message);
    }

}
