package com.mrlu.rabbitmq.routing;

import com.mrlu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

/**
 * @author 简单de快乐
 * @date 2021-05-21 13:24
 *
 * Routing之订阅模型-Direct（直连）
 *
 * 特点：
 * （1）队列和交换机的绑定，不是任意绑定的，而是要指定一个RoutingKey（路由key）
 * （2）消息的发送方在向Exchange（交换机）发送消息时，也必须指定消息的RoutingKey
 * （3）Exchange不再把消息交给每一个绑定的队列，而是根据消息的Routing Key进行判断
 *     只有队列的RoutingKey和消息的RoutingKey完全一致，才会接收到消息
 *
 */
public class Producer {

    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMqUtils.getConnection("192.168.187.100", 5672,
                "/01-rabbitmq", "lu", "12345");
        Channel channel = connection.createChannel();

        //通过通道声明交换机 参数1：交换机的名称 参数2：交换机的类型 Direct模式下设置为 direct 路由模式
        String exchangeName = "01-hello-routing-direct";
        //其实生产者和消费者有一个建立交换机就行了。
        channel.exchangeDeclare(exchangeName,"direct");

        //发送消息
        String routingKey = "info";
        //String routingKey = "error";
        String message = "这是direct模型基于route key：【"+routingKey+"】发送的消息";
        channel.basicPublish(exchangeName,routingKey,null,message.getBytes("utf-8"));

        //关闭资源
        RabbitMqUtils.closeChannelAndConnection(channel,connection);
    }
}
