package com.mrlu.rabbitmq.fanout;

import com.mrlu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

/**
 * @author 简单de快乐
 * @date 2021-05-21 12:31
 *
 * 扇出模型（广播模型）：发布订阅
 *  特点：
 *   （1）可以有多个消费者
 *   （2）每个消费者有自己的queue（队列）
 *   （3）每个队列都要绑定到Exchange（交换机）
 *   （4）生成者发送的消息，只能发送到交换机，交换机来决定要发送给哪个队列，生成者无法决定
 *   （5）交换机把消息发送给绑定过的所有队列
 *   （6）队列的消费者都能拿到消息，实现一条消息被多个消费者消费。
 *
 *    如果没有队列绑定到交换，消息将丢失，但这对我们来说没关系；（所以要先启动交换机）
 *    如果没有消费者正在收听，我们也可以安全地丢弃该消息。
 */
public class Producer {

    public static void main(String[] args) {
        Connection connection = RabbitMqUtils.getConnection("192.168.187.100",
                5672,
                "/01-rabbitmq", "lu", "12345");

        try {
            Channel channel = connection.createChannel();

            //1、将通道声明指定的交换机.该方法：如果没有交换机就创建，有就不创建
            //参数1：交换机的名称 参数2 交换机的类型 广播模式下固定为fanout
            channel.exchangeDeclare("01-hello-fanout","fanout");

            //发送消息,广播模式routingKey设置为""
            channel.basicPublish("01-hello-fanout","",
                    null,"hello fanout".getBytes("UTF-8"));

            //释放资源
            RabbitMqUtils.closeChannelAndConnection(channel,connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
