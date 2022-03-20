package com.mrlu.rabbitmq.deathqueue;

import com.mrlu.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

/**
 * @author 简单de快乐
 * @date 2021-07-07 21:21
 */
public class Consumer02 {
    /**
     * 死信队列的名称
     */
    private static final String DEAD_QUEUE = "dead_queue";

    static Connection connection = RabbitMqUtils.getConnection("192.168.187.100",
            5672, "/", "admin", "123");

    public static void main(String[] args) throws IOException {
        Channel channel = connection.createChannel();
        System.out.println("等待接受消息...");
        channel.basicConsume(DEAD_QUEUE, true,
                (consumerTag, message) -> System.out.println("Consumer02消费："+
                        new String(message.getBody())),
                consumerTag -> {});
    }
}
