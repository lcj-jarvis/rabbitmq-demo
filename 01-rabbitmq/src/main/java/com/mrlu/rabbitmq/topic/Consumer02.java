package com.mrlu.rabbitmq.topic;

import com.mrlu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author 简单de快乐
 * @date 2021-05-21 14:08
 */
public class Consumer02 {

    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMqUtils.getConnection("192.168.187.100", 5672,
                "/01-rabbitmq", "lu", "12345");
        Channel channel = connection.createChannel();

        //声明交换机以及交换机的类型
        String exchangeName = "01-hello-topic";
        channel.exchangeDeclare(exchangeName,"topic");

        //创建临时队列
        String queueName = channel.queueDeclare().getQueue();

        //绑定交换机和队列 动态通配符形式的route key  #:可以匹配0到任意多个字符
        channel.queueBind(queueName,exchangeName,"*.item.#");
        channel.queueBind(queueName,exchangeName,"*.*.*.save");

        //消费消息
        channel.basicConsume(queueName,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者1："+new String(body));
            }
        });
    }
}
