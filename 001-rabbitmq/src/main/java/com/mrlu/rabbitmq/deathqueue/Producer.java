package com.mrlu.rabbitmq.deathqueue;

import com.mrlu.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

/**
 * @author 简单de快乐
 * @date 2021-07-07 21:11
 * 测试的时候，先启动消费者一，创建交换机和队列。
 *    然后把消费者一停掉，让消息过期。
 *
 * 【注：测试时要去rabbitmq的客户端界面删除队列的交换机和队列】
 *
 *  延迟队列可以认为是死信队列中，设置了消息的TTL时间，没有消费者一，只有消费者二的情景。
 *  延迟队列的应用场景：
 *  1.订单在十分钟之内未支付则自动取消
 *  2.新创建的店铺，如果在十天内都没有上传过商品，则自动发送消息提醒。
 *  3.用户注册成功后，如果三天内没有登陆则进行短信提醒。
 *  4.用户发起退款，如果三天内没有得到处理则通知相关运营人员。
 *  5.预定会议后，需要在预定的时间点前十分钟通知各个与会人员参加会议
 *    ...
 *
 *  为什么不使用定时任务呢？
 *  如果数据量很大的话，采用定时任务的方式，性能就会很低
 *
 *  TTL 是什么呢？
 *  更加准确的说 TTL是RabbitMQ 中一个消息或者队列的属性，
 *  表明一条消息或者该队列中的所有消息的最大存活时间。
 */
public class Producer {

    /**
     * 普通交换机的名称
     */
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    static Connection connection = RabbitMqUtils.getConnection("192.168.187.100",
            5672, "/", "admin", "123");

    public static void main(String[] args) throws IOException {
        Channel channel = connection.createChannel();

        //测试因为消息TTL过期而变成死信
        //死信消息，设置TTL时间，单位是ms TTL:time to live 消息生存
        /*AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
                                               .builder().expiration("10000").build();*/
        for (int i = 0; i < 10; i++) {
            String message = "消息" + i;
            System.out.println("发送：" + message);
            /*channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", basicProperties,
                    message.getBytes());*/
            //测试队列达到最大长度
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null,
                    message.getBytes());
        }
    }
}
