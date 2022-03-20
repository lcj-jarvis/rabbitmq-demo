package com.mrlu.rabbitmq.routing;

import com.mrlu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author 简单de快乐
 * @date 2021-05-21 13:36
 */
public class Consumer01 {

    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMqUtils.getConnection("192.168.187.100", 5672,
                "/01-rabbitmq", "lu", "12345");
        Channel channel = connection.createChannel();

        //1、通道声明交换机以及交换机的类型
        String exchangeName = "01-hello-routing-direct";
        channel.exchangeDeclare(exchangeName,"direct");

        //2、创建一个临时队列
        String queueName = channel.queueDeclare().getQueue();

        //3、基于route key 绑定交换机和队列
        channel.queueBind(queueName,exchangeName,"info");
        channel.queueBind(queueName,exchangeName,"error");
        channel.queueBind(queueName,exchangeName,"warning");

        //4、获取消费的信息
        channel.basicConsume(queueName,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者1："+new String(body));
            }
        });
    }
}
