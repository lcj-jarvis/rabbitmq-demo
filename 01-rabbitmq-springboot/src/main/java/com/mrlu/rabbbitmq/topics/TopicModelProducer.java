package com.mrlu.rabbbitmq.topics;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 简单de快乐
 * @date 2021-05-21 17:52
 */
@Controller
public class TopicModelProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/user/topics")
    public void sendMessage(String message){
        rabbitTemplate.convertAndSend("01-hello-springboot-topic-exchange",
                "user.order.add",message+" user.order.add");
        rabbitTemplate.convertAndSend("01-hello-springboot-topic-exchange",
                "user.order.book.unpaid",message+" user.order.book.unpaid");
        rabbitTemplate.convertAndSend("01-hello-springboot-topic-exchange",
                "manager.add",message+" manager.add");
    }
}
