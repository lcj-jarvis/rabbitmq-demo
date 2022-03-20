package com.mrlu.rabbitmq.helloworld;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 简单de快乐
 * @date 2021-07-05 23:00
 */
public class Consumer {

    public static final String QUEUE_NAME = "001-hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.187.100");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("======消费者标签=====>" + consumerTag);
            System.out.println(new String(message.getBody()));
        };
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息消费中断");
        };

        /**
         *  消费者消费消息
         *  1、参数一：消费哪个队列
         *  2、参数二：消费成功之后是否要自动应答，true表示自动应答，false反之
         *  3、参数三：推送的消息如何进行消费的接口回调
         *  4、参数四：取消消费的一个回调接口，如在消费的时候队列对删除掉了
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
