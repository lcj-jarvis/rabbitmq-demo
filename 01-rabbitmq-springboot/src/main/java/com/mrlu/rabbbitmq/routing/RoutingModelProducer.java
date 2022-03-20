package com.mrlu.rabbbitmq.routing;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 简单de快乐
 * @date 2021-05-21 17:36
 */
@Controller
public class RoutingModelProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/user/routing")
    public void sendMessage(String message){
        //参数1：交换机的名称
        //参数2：路由的key
        //参数3：发送的消息
        /*rabbitTemplate.convertAndSend("01-rabbitmq-springboot-routing-direct-exchange",
                "info",message);*/
        rabbitTemplate.convertAndSend("01-rabbitmq-springboot-routing-direct-exchange",
                "error",message);
    }
}
