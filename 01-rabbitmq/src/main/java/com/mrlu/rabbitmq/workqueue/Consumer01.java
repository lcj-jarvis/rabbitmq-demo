package com.mrlu.rabbitmq.workqueue;

import com.mrlu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author 简单de快乐
 * @date 2021-05-20 22:27
 */
public class Consumer01 {
    public static void main(String[] args) {
        Connection connection = RabbitMqUtils.getConnection("192.168.187.100", 5672,
                "/01-rabbitmq", "lu", "12345");
        try {
            Channel channel = connection.createChannel();
            //通道每次只能消费一个消息
            channel.basicQos(1);
            channel.queueDeclare("01-hello-workqueue",true,false,false,null);

            //将自动确认修改为false。
            channel.basicConsume("01-hello-workqueue",false, new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("=========>"+new String(body));
                    //手动确认消息。multiple为true的话，表示一次确认完通道里的所有消息，反之一次只能确认一个。
                    channel.basicAck(envelope.getDeliveryTag(),false);

                    //如果被拒绝的消息应该重新排队而不是丢弃/死信，则为 true
                    //channel.basicReject(long deliveryTag, boolean requeue);
                    // multiple 为true的话，表示将通道里的所有消息一次拒绝。反之一次只能拒绝一个
                    //channel.basicNack(long deliveryTag, boolean multiple, boolean requeue);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
