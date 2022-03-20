package com.mrlu.rabbitmq.helloworld;

import com.mrlu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

/**
 * @author 简单de快乐
 * @date 2021-05-19 22:09
 *
 *  直连模式
 */
public class Consumer01 {

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
//        ConnectionFactory connectionFactory = new ConnectionFactory();
//        connectionFactory.setHost("192.168.187.100");
//        connectionFactory.setPort(5672);
//
//        connectionFactory.setVirtualHost("/01-rabbitmq");
//        connectionFactory.setUsername("lu");
//        connectionFactory.setPassword("12345");

        //创建连接
        //Connection connection = connectionFactory.newConnection();
        Connection connection = RabbitMqUtils.getConnection("192.168.187.100", 5672,
                "/01-rabbitmq","lu","12345");
        //创建通道
        Channel channel = connection.createChannel();
        //通道绑定队列.注意这个队列的参数要和生成者的放送消息的队列一样
        //channel.queueDeclare("01-rabbitmq-hello",false,false,false,null);
        channel.queueDeclare("01-rabbitmq-hello",true,false,true,null);

        //消费消息
        //参数1：消费的那个队列的名称
        //参数2：开始消息的自动确认机制
        //参数3：消费时的回调接口
        //channel.basicConsume("01-rabbitmq-hello",true,new DefaultConsumer(channel){
        channel.basicConsume("01-rabbitmq-hello01",true,
                new DefaultConsumer(channel){
            //方法的最后一个参数：消息队列中取出的消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("======>"+ new String(body));
            }
        });

        //注意：消费者要不断的监听，不要关闭，因为关闭只要一瞬间，有可能消息还没有消费完。
        // channel.close();
        // connection.close();
    }

}
