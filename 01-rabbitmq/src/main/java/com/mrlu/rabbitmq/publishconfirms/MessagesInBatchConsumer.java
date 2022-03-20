package com.mrlu.rabbitmq.publishconfirms;

import com.mrlu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

/**
 * @author 简单de快乐
 * @date 2021-05-22 23:17
 */
public class MessagesInBatchConsumer {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMqUtils.getConnection("192.168.187.100", 5672,
                "/01-rabbitmq", "lu", "12345");
        Channel channel = connection.createChannel();
        String queue = "Batch";
        channel.queueDeclare(queue, false, false, true, null);

        channel.basicQos(1);
        channel.basicConsume(queue, false, (consumerTag, message) -> {
            System.out.println("MessagesInBatchConsumer："+new String(message.getBody()));
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        }, consumerTag -> {});

    }
}
