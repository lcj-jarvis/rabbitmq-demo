package com.mrlu.rabbitmq.produce;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 简单de快乐
 * @date 2021-05-21 21:47
 */
@RestController
public class TopicProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/mirror-cluster/test")
    @ResponseBody
    public String sendDirectMessage(){

        String messageId = UUID.randomUUID().toString().substring(0,5);
        String messageData = "test message,hello";
        String createTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss SSS"));

        Map<String, Object> data = new HashMap<>(16);
        data.put("messageId",messageId);
        data.put("messageData",messageData);
        rabbitTemplate.convertAndSend("TopicExchange",
                "user.order.book.unpaid",data);

        data.put("createTime",createTime);
        rabbitTemplate.convertAndSend("TopicExchange",
                "manager.add",data);

        return "ok";
    }
}
