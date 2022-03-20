package com.mrlu.rabbbitmq.fanout;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 简单de快乐
 * @date 2021-05-21 17:18
 */
@Controller
public class FindOutModelProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/user/fanout")
    public void sendMessage(String message){
        //第一个参数为交换机的名称，第二个参数为路由的key，findout模型设置为""
        rabbitTemplate.convertAndSend("01-rabbitmq-springboot-fanout-exchange",
                "",message);
    }
}
