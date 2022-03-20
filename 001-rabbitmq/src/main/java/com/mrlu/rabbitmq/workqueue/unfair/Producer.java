package com.mrlu.rabbitmq.workqueue.unfair;

import com.mrlu.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.Scanner;

/**
 * @author 简单de快乐
 * @date 2021-07-06 20:37
 *
 *  手动确认，实现能者多劳
 */
public class Producer {

    private static final String QUEUE_NAME="work-queue";

    public static void main(String[] args) throws Exception {
        Connection connection = RabbitMqUtils.getConnection("192.168.187.100",
                5672, "/", "admin", "123");
        try(Channel channel= connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME,false,false,
                    false,null);
            for (int i = 0; i < 20; i++) {
                String message = "消息：" + i;
                channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
                System.out.println("发送消息完成:" + message);

            }
        }
    }
}
