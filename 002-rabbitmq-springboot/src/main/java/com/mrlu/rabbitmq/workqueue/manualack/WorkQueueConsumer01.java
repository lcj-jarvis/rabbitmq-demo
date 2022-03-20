package com.mrlu.rabbitmq.workqueue.manualack;

import com.mrlu.rabbitmq.config.workqueue.WorkQueueConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author 简单de快乐
 * @date 2021-07-11 15:32
 */
@Component
@Slf4j
public class WorkQueueConsumer01 {

    @RabbitListener(queues = WorkQueueConfig.MANUAL_QUEUE)
    public void receive(Channel channel, Message message) throws IOException {
        try {
            //channel.basicQos(1);

            log.info("消费者1收到消息{}", new String(message.getBody()));
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //手动确认消息
            //第二个参数为是否批量确认通道里的消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            //拒绝确认消息
            //第二个参数为是否批量拒绝通道里的消息，第三个参数为是否将未确认的消息重新入队
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            log.info("消费者1未确认的消息{}", new String(message.getBody()));

        }
    }
}
