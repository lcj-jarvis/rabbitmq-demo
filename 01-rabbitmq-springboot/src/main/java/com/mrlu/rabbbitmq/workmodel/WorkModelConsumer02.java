package com.mrlu.rabbbitmq.workmodel;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author 简单de快乐
 * @date 2021-05-21 19:39
 *
 * 能者多劳的实现
 *
 * #能者多劳要在spring的配置文件中加入以下的配置
 * #发送确认
 * #spring.rabbitmq.publisher-confirm-type=correlated
 * #发送回调
 * #spring.rabbitmq.publisher-returns=true
 * ##消费手动确认
 * #spring.rabbitmq.listener.direct.acknowledge-mode=manual
 * #spring.rabbitmq.listener.simple.acknowledge-mode=manual
 * #并发消费者初始化值
 * #spring.rabbitmq.listener.simple.concurrency=1
 * #并发消费者的最大值
 * #spring.rabbitmq.listener.simple.max-concurrency=10
 * #每个消费者每次监听时可拉取处理的消息数量
 * #在单个请求中处理的消息个数，他应该大于等于事务数量（unack的最大数量）
 * #spring.rabbitmq.listener.simple.prefetch=1
 * #是否支持重试
 * #spring.rabbitmq.listener.simple.retry.enabled=true
 *
 */
@Component
public class WorkModelConsumer02 {

    @RabbitListener(queuesToDeclare = @Queue(value = "01-rabbitmq-springboot-workmodel",durable = "true"))
    public void receive1(String info, Message message, Channel channel) throws IOException {
        try {
            //确认一条消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            System.out.println("消费者1==》"+info);
        } catch (IOException e) {
            //消费者告诉消息队列信息消费失败

            /**
             * 拒绝确认消息
             * basicNack(long var1, boolean var3, boolean var4)
             * deliveryTag：为该消息的index
             * multiple：是否批量 true表示将一次性拒绝小于deliveryTag的消息
             * requeue：被拒绝的是否重新加入队列
             */
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);

            //参数说明：deliveryTag：为该消息的index requeue：被拒绝的是否重新加入队列。
            //basicReject一次只能拒绝单条消息
            //channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }

    @RabbitListener(queuesToDeclare = @Queue(value = "01-rabbitmq-springboot-workmodel",durable = "true"))
    public void receive2(String info, Message message, Channel channel) throws IOException {
        try {
            //确认一条消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            System.out.println("消费者2==》"+info);
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            //消费者告诉消息队列信息消费失败
            /**
             * 拒绝确认消息
             * basicNack(long var1, boolean var3, boolean var4)
             * deliveryTag：为该消息的index
             * multiple：是否批量 true表示将一次性拒绝小于deliveryTag的消息
             * requeue：被拒绝的是否重新加入队列
             */
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),
                    false,true);
        }
    }
}
