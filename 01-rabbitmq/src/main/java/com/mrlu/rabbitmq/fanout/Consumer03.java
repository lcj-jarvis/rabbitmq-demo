package com.mrlu.rabbitmq.fanout;

import com.mrlu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author 简单de快乐
 * @date 2021-05-21 12:56
 */
public class Consumer03 {

    public static void main(String[] args) {
        Connection connection = RabbitMqUtils.getConnection("192.168.187.100", 5672,
                "/01-rabbitmq", "lu", "12345");
        try {
            Channel channel = connection.createChannel();
            //1、通道绑定交换机
            channel.exchangeDeclare("01-hello-fanout","fanout");

            //2、通道绑定交换机和队列
            //临时队列
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName,"01-hello-fanout","");

            //消费消息
            channel.basicConsume(queueName,true,new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("消费者3："+new String(body));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
