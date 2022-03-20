package com.mrlu.rabbitmq.priorityqueue;

import com.mrlu.rabbitmq.config.priorityqueue.PriorityQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 简单de快乐
 * @date 2021-07-09 23:50
 */
@Component
@Slf4j
public class PriorityQueueConsumer {

    @RabbitListener(queues = PriorityQueueConfig.PRIORITY_QUEUE)
    public void receive(Message message) {
        log.info("收到消息：{}",new String(message.getBody()));
    }
}
