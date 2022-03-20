package com.mrlu.rabbitmq.rpc;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 简单de快乐
 * @date 2021-05-23 12:59
 */
@RestController
public class RPCClient {

     @Autowired
     private RabbitTemplate rabbitTemplate;

     @GetMapping("rpc/request")
     public String sendRequest(Integer number){
         System.out.println(" [x] Requesting fib(" + number + ")");
         //第一个参数为交换机的名字，第二个参数为routingKey
         Integer response = (Integer) rabbitTemplate
                 .convertSendAndReceive("tut.rpc","rpc",number);
         System.out.println(" [.] Got '" + response + "'");
         return "ok";
     }
}
