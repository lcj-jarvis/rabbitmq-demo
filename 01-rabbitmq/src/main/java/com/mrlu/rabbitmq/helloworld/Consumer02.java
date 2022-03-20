package com.mrlu.rabbitmq.helloworld;

import com.mrlu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;

/**
 * @author 简单de快乐
 * @date 2021-05-21 12:12
 */
public class Consumer02 {

    public static void main(String[] args) {
        Connection connection = RabbitMqUtils.getConnection("192.168.187.100", 5672,
                "/01-rabbitmq", "lu", "12345");
        try {
            Channel channel = connection.createChannel();
            channel.queueDeclare("01-rabbitmq-hello01", true, false, true, null);
            channel.basicQos(1);

            //这样接收消息也行
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                System.out.println(new String(delivery.getBody()));
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            };
            channel.basicConsume("01-rabbitmq-hello01",false,deliverCallback,consumerTag -> {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
