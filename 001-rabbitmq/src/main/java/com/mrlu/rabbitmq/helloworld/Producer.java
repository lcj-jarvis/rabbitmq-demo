package com.mrlu.rabbitmq.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 简单de快乐
 * @date 2021-07-05 22:43
 */
public class Producer {

    public static final String QUEUE_NAME = "001-hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //创建连接
        factory.setHost("192.168.187.100");
        factory.setUsername("admin");
        factory.setPassword("123");

        Connection connection = factory.newConnection();
        //Channel实现了AutoCloseable接口，会自动关闭
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
        String message = "hello world";

        /**
         * 发送一个消息
         * 1、发送到哪个交换机.注意如果没有，不能写成null，要写成空串
         * 2、路由的key
         * 3、其他的参数
         * 4、发送的消息的消息体
         */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

        System.out.println("发送完毕");
    }
}
