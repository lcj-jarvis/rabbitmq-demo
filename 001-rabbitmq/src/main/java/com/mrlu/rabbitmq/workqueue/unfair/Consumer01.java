package com.mrlu.rabbitmq.workqueue.unfair;

import com.mrlu.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.util.concurrent.TimeUnit;

/**
 * @author 简单de快乐
 * @date 2021-07-06 20:40
 */
public class Consumer01 {

    private static final String QUEUE_NAME = "work-queue";

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitMqUtils.getConnection("192.168.187.100",
                5672,
                "/", "admin", "123");
        Channel channel = connection.createChannel();
        //将预先值prefetchCount设置为 1。非公平分发可以设置为1
        channel.basicQos(2);
        DeliverCallback deliverCallback=(consumerTag, delivery)->{
            try {
                TimeUnit.SECONDS.sleep(1);
                String receivedMessage = new String(delivery.getBody());
                System.out.println("接收到消息:"+receivedMessage);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        };
        CancelCallback cancelCallback=(consumerTag)->{
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        };
        System.out.println("C1 消费者启动等待消费......");

        //autoAck: 自动确认
        channel.basicConsume(QUEUE_NAME,false,deliverCallback,cancelCallback);
    }
}
