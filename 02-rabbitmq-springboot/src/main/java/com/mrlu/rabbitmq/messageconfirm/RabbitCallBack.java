package com.mrlu.rabbitmq.messageconfirm;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 简单de快乐
 * @date 2021-05-22 0:21
 */
@RestController
public class RabbitCallBack {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 情况1测试 ：
     * 消息发送到server，但是在server中找不到交换机，触发ConfirmCallback回调函数
     * @return
     */
    @GetMapping("/TestMessageAck1")
    public String sendDirectMessage1(){
        String messageId = UUID.randomUUID().toString().substring(0,5);
        String messageData = "test message,hello";
        String createTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss SSS"));

        Map<String, Object> data = new HashMap<>();
        data.put("messageId",messageId);
        data.put("messageData",messageData);
        data.put("createTime",createTime);
        //发送消息
        rabbitTemplate.convertAndSend("no exist exchange",
                "TestDirectRouting1",data);

        return "ok";
    }

    /**
     *  情形2测试：
     *  消息推送到server，找到了交换机，但是没有找到队列，(即交换机和队列没有建立通道连接)
     *  会触发ConfirmCallback和ReturnsCallback回调函数
     * @return
     */
    @GetMapping("/TestMessageAck2")
    public String sendDirectMessage2(){
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
                "lonelyDirectExchange",data);

        return "ok";
    }

    @GetMapping("/TestMessageAck3")
    public String sendDirectMessage3(){
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
