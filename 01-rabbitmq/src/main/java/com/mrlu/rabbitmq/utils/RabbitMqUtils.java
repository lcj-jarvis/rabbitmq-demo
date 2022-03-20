package com.mrlu.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 简单de快乐
 * @date 2021-05-20 21:02
 */
public class RabbitMqUtils {

    private static ConnectionFactory connectionFactory;
    static{
        connectionFactory = new ConnectionFactory();
    }

    /**
     *
     * @param hostIp RabbitMQ 所在的主机的ip地址
     * @param hostRabbitMqPort RabbitMQ的客户端的端口号
     * @param virtualHost 虚拟主机名称
     * @param username 虚拟主机的用户名
     * @param password 虚拟主机的ip地址
     * @return
     */
    public static Connection getConnection(String hostIp,Integer hostRabbitMqPort,String virtualHost,String username,String password){
        try {
            connectionFactory.setHost(hostIp);
            connectionFactory.setPort(hostRabbitMqPort);
            connectionFactory.setVirtualHost(virtualHost);
            connectionFactory.setUsername(username);
            connectionFactory.setPassword(password);
            return connectionFactory.newConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭管道和连接
     * @param channel
     * @param connection
     */
    public static void closeChannelAndConnection(Channel channel,Connection connection){
        if (channel != null) {
            try {
                channel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (connection != null){
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
