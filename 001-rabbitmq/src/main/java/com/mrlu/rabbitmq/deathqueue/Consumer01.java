package com.mrlu.rabbitmq.deathqueue;

import com.mrlu.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 简单de快乐
 * @date 2021-07-07 20:50
 *
 *
 */
public class Consumer01 {

    /**
     * 普通交换机的名称
     */
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    /**
     * 死信交换机的名称
     */
    private static final String DEAD_EXCHANGE = "dead_exchange";
    /**
     * 普通队列的名称
     */
    private static final String NORMAL_QUEUE = "normal_queue";
    /**
     * 死信队列的名称
     */
    private static final String DEAD_QUEUE = "dead_queue";

    static Connection connection = RabbitMqUtils.getConnection("192.168.187.100",
            5672, "/", "admin", "123");

    public static void main(String[] args) throws IOException {
        //获取通道
        Channel channel = connection.createChannel();

        //声明普通交换机和死信交换机，类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE, true, false, false, null);
        //死信队列绑定死信交换机
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        //创建正常队列，正常队列绑定死信交换机
        Map<String, Object> arguments = new HashMap<>(16);
        //正常队列设置死信交换机，参数key是固定值
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //正常队列设置死信routingKey，参数key是固定值
        arguments.put("x-dead-letter-routing-key", "lisi");

        //测试队列达到最大长度，而导致消息变成死信。这里设置队列的最大长度为6
        //arguments.put("x-max-length", 6);

        channel.queueDeclare(NORMAL_QUEUE, true, false, false, arguments);
        //正常队列绑定正常的交换机
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");

        //消费消息
        /*channel.basicConsume(NORMAL_QUEUE, true, (consumerTag, message) ->
                System.out.println("Consumer01消费：" + new String(message.getBody())),
                (consumerTag -> {}));*/

        //测试消息被拒绝，而且不重新入队
        channel.basicConsume(NORMAL_QUEUE, false, (consumerTag, message) -> {
                    String msg = new String(message.getBody());
                    if ("消息4".equals(msg)) {
                        //拒绝，并且不重新入队，从而变成死信
                        channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
                        System.out.println("变成死信的消息：" + msg);
                    } else {
                        System.out.println("Consumer01消费：" + msg);
                    }
                },
                (consumerTag -> {}));
    }
}
