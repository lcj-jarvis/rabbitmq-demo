package com.mrlu.rabbbitmq.workmodel;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author 简单de快乐
 * @date 2021-05-21 16:41
 *
 */
@Controller
public class WorkModelProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/user/work")
    public void sendMessage(@RequestParam("message") String message){
        System.out.println(message);
        for (int i = 0; i < 20; i++) {
            rabbitTemplate.convertAndSend("01-rabbitmq-springboot-workmodel",i+message);
        }
    }
}
