package com.mrlu.rabbitmq.delayqueue;

import com.mrlu.rabbitmq.config.delayqueue.DelayQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author 简单de快乐
 * @date 2021-07-08 22:37
 */
@Component
@Slf4j
public class DelayQueueConsumer {

    @RabbitListener(queues = DelayQueueConfig.DELAY_QUEUE_NAME)
    public void receive(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间：{}，收到延迟队列的消息：{}", LocalDateTime.now().toString(), msg);
    }
}
