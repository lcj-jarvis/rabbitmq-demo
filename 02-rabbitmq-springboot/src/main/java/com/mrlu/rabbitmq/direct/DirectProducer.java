package com.mrlu.rabbitmq.direct;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 简单de快乐
 * @date 2021-05-21 21:09
 */
@RestController
public class DirectProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendDirectMessage")
    public String sendDirectMessage(){
        String messageId = UUID.randomUUID().toString().substring(0,5);
        String messageData = "test message,hello";
        String createTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss SSS"));

        Map<String, Object> data = new HashMap<>();
        data.put("messageId",messageId);
        data.put("messageData",messageData);
        data.put("createTime",createTime);
        //发送消息
        rabbitTemplate.convertAndSend("TestDirectExchange",
                "TestDirectRouting1",data);
        data.put("TestDirectRouting2","TestDirectRouting2");
        rabbitTemplate.convertAndSend("TestDirectExchange",
                "TestDirectRouting2",data);

        return "ok";
    }
}
