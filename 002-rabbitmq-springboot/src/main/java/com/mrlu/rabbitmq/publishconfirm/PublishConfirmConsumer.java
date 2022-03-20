package com.mrlu.rabbitmq.publishconfirm;

import com.mrlu.rabbitmq.config.publishconfirm.PublishConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 简单de快乐
 * @date 2021-07-09 22:21
 */
@Component
@Slf4j
public class PublishConfirmConsumer {

    @RabbitListener(queues = PublishConfirmConfig.CONFIRM_QUEUE)
    public void receive(Message message) {
        log.info("收到消息：{}", new String(message.getBody()));

    }
}
