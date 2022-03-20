package com.mrlu.rabbitmq.workqueue;

import com.mrlu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author 简单de快乐
 * @date 2021-05-20 22:27
 */
public class Consumer02 {
    public static void main(String[] args) {
        Connection connection = RabbitMqUtils.getConnection("192.168.187.100", 5672,
                "/01-rabbitmq", "lu", "12345");
        try {
            Channel channel = connection.createChannel();
            //通道channel每次只能消费一个消息
            channel.basicQos(1);
            channel.queueDeclare("01-hello-workqueue",true,false,false,null);
            //修改为手动确认
            channel.basicConsume("01-hello-workqueue",false, new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("=========>"+new String(body));
                    // 参数1：确认队列中的具体的消息。
                    // 参数2：是否开启多个消息同时确认。即一次只能确认一个消息
                    // 如果没有手动确认的话，RabbitMQ的界面的Queues,
                    // 会发现队列的Unacked会显示多少个没有被删除，此时队列中也还会剩下没有被删除的
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
