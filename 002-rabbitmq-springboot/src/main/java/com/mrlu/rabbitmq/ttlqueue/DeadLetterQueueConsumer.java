package com.mrlu.rabbitmq.ttlqueue;

import com.mrlu.rabbitmq.config.ttl.TtlQueueConfig;
import com.rabbitmq.client.AMQP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author 简单de快乐
 * @date 2021-07-08 21:27
 */
@Slf4j
@Component
public class DeadLetterQueueConsumer {

    @RabbitListener(queues = TtlQueueConfig.DEAD_LETTER_QUEUE)
    public void receive(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间：{}，收到死信队列的消息：{}", LocalDateTime.now().toString(), msg);
    }
}
