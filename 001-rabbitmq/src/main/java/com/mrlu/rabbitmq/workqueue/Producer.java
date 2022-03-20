package com.mrlu.rabbitmq.workqueue;

import com.mrlu.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.Scanner;

/**
 * @author 简单de快乐
 * @date 2021-07-06 20:37
 *
 *
 */
public class Producer {

    private static final String QUEUE_NAME="work-queue";

    public static void main(String[] args) throws Exception {
        Connection connection = RabbitMqUtils.getConnection("192.168.187.100",
                5672, "/", "admin", "123");
        try(Channel channel= connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME,false,false,
                    false,null);
            //从控制台当中接受信息
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()){
                String message = scanner.next();
                channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
                System.out.println("发送消息完成:" + message);
            }
        }
    }
}
