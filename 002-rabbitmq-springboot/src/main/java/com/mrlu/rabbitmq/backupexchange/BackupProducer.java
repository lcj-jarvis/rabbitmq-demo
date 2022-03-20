package com.mrlu.rabbitmq.backupexchange;

import com.mrlu.rabbitmq.config.backup.BackupConfig;
import com.mrlu.rabbitmq.config.publishconfirm.PublishConfirmConfig;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

/**
 * @author 简单de快乐
 * @date 2021-07-09 21:40
 */
@Controller
public class BackupProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/backup/{msg}")
    public void sendMessage(@PathVariable("msg")String message) {
        String id = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(id);
        //消息发送不到队列
        rabbitTemplate.convertAndSend(BackupConfig.BACKUP_EXCHANGE, "不存在的routingKey",
                message + "不存在的routingKey", correlationData);
    }
}
