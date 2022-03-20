package com.mrlu.rabbitmq.topic;

import com.mrlu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

/**
 * @author 简单de快乐
 * @date 2021-05-21 13:57
 *
 * Routing之订阅模型-Topic
 * Topic模型的Exchange和direct相比，都是可以根据RoutingKey把消息路由到不同的队列。只不过Topic类型的
 * Exchange可以让队列在绑定Routing key的时候使用通配符。这种模型的Routingkey一般是由一个或多个单词组成，
 * 多个单词之间以“.”分割。例如："user.item.save"
 *
 * 动态路由的通配符匹配
 *     * ：只能匹配1个单词
 *     #：可以匹配0到任意多个单词
 *
 * 【注意***】
 *    （1）Topic模型的路由key最长255个字节
 *
 *    （2）如果有一个队列绑定了两个routing key，而这两个routing key都和
 *        发送消息的routing key 匹配，那么只会发送一次消息到该队列
 */
public class Producer {

    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMqUtils.getConnection("192.168.187.100", 5672,
                "/01-rabbitmq", "lu", "12345");
        Channel channel = connection.createChannel();

        //声明交换机以及交换机的类型 topic模型下，交换机的类型只能为topic
        String exchangeName = "01-hello-topic";
        channel.exchangeDeclare(exchangeName,"topic");

        //发布消息
        //String routingKey = "user.item.save";
        String routingKey = "user.item.book.save";
        String message = "Topic模型基于route key【"+routingKey+"】发布的消息";
        channel.basicPublish(exchangeName,routingKey,null,
                message.getBytes("UTF-8"));

        RabbitMqUtils.closeChannelAndConnection(channel,connection);
    }
}
