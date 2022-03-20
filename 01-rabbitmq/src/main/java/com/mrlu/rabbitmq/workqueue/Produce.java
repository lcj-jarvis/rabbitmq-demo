package com.mrlu.rabbitmq.workqueue;

import com.mrlu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

/**
 * @author 简单de快乐
 * @date 2021-05-20 22:20
 *
 * work queue 任务模型
 *    让多个消费者绑定到一个队列，共同消费队列中的消息。
 * 总结：默认情况下，RabbitMQ将按照顺序将每个消息发送给下一个使用者。平均而言，每
 *      个消费者都会收到相同数量的消息（这种分发消息的方式称为循环）
 *      但是这种有一个缺点就是，消费快的消费者一下子消费完了，
 *      但是消费慢的却没有消费完。不能达到能者多劳的效果，消费快的去
 *      帮助消费慢的消费。
 *
 *      平均分配和消息的自动确认机制有关
 *
 * 实现能者多劳：
 * 1、将自动缺设置为false。同时手动确认
 * 2、将通道channel一次只能设置为每次只能消费一个消息(可以理解为通道里一次最多存放一个消息)
 */
public class Produce {

    public static void main(String[] args) {
        Connection connection = RabbitMqUtils.getConnection("192.168.187.100", 5672,
                "/01-rabbitmq", "lu", "12345");
        try {
            Channel channel = connection.createChannel();
            channel.queueDeclare("01-hello-workqueue",true,false,
                    false,null);

            for (int i = 0; i < 50; i++) {
                channel.basicPublish("","01-hello-workqueue",null,(i+"hello workqueue").getBytes());
            }
            RabbitMqUtils.closeChannelAndConnection(channel,connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
