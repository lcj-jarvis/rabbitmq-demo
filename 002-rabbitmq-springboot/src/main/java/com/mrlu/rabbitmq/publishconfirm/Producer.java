package com.mrlu.rabbitmq.publishconfirm;

import com.mrlu.rabbitmq.config.publishconfirm.PublishConfirmConfig;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 简单de快乐
 * @date 2021-07-09 21:40
 */
@Controller
public class Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/confirm/{msg}")
    public void sendMessage(@PathVariable("msg")String message) {

        //正常发送
        CorrelationData correlationData = new CorrelationData("消息0");
        //消息发送不到交换机
        rabbitTemplate.convertAndSend(PublishConfirmConfig.CONFIRM_EXCHANGE, PublishConfirmConfig.CONFIRM_ROUTING_KEY,
                message + "正常发送", correlationData);

        CorrelationData correlationData1 = new CorrelationData("消息1");
        //消息发送不到交换机
        rabbitTemplate.convertAndSend("不存在的交换机", PublishConfirmConfig.CONFIRM_ROUTING_KEY,
                message + "不存在的交换机", correlationData1);

        CorrelationData correlationData2 = new CorrelationData("消息2");
        //消息发送不到队列
        rabbitTemplate.convertAndSend(PublishConfirmConfig.CONFIRM_EXCHANGE, "不存在的routingKey",
                message + "不存在的routingKey", correlationData2);
    }
}
