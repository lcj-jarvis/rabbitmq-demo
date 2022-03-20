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
public class Consumer02 {

    private static final String QUEUE_NAME = "work-queue";

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitMqUtils.getConnection("192.168.187.100",
                5672,
                "/", "admin", "123");
        Channel channel = connection.createChannel();
        //将预先值prefetchCount设置为 1
        // 可以理解一开始通道中最多能存放的消息。当达到最多的数量时候，就不继续添加。
        // 之后根据执行效率来看，就是能者多劳的情形了。
        // 如 Consumer02 prefetchCount 的数量为 5 ，Consumer01 prefetchCount 的数量为 1
        // 如果生产者发送7条消息，即使Consumer02的执行效率很低，Consumer01的效率很高，但是一开始
        // 也是分发给Consumer02的通道5条消息，Consumer01的通道2条消息。

        // 如果有7条以上的消息，前7条是按照上面的逻辑分发，7条后面的就是按照能者多劳分发

        //希望开发人员能限制此
        //缓冲区的大小，以避免缓冲区里面无限制的未确认消息问题。这个时候就可以通过使用 basic.qos 方法设
        //置“预取计数”值来完成的, 该值定义通道上允许的未确认消息的最大数量。
        channel.basicQos(5);
        DeliverCallback deliverCallback=(consumerTag, delivery)->{
            try {
                TimeUnit.SECONDS.sleep(5);
                String receivedMessage = new String(delivery.getBody());
                System.out.println("接收到消息:"+receivedMessage);
                //手动确认
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        };
        CancelCallback cancelCallback=(consumerTag)->{
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");};
        System.out.println("C2 消费者启动等待消费......");

        //autoAck: 自动确认
        channel.basicConsume(QUEUE_NAME,false,deliverCallback,cancelCallback);

    }
}
