package com.mrlu.rabbitmq.rpc;

/**
 * @author 简单de快乐
 * @date 2021-05-23 0:30
 */
import com.mrlu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.*;

public class RPCServer {

    private static final String RPC_QUEUE_NAME = "rpc_queue";

    private static int fib(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return fib(n - 1) + fib(n - 2);
    }

    public static void main(String[] argv) throws Exception {

        try (Connection connection = RabbitMqUtils.getConnection("192.168.187.100", 5672,
                "/01-rabbitmq", "lu", "12345");
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
            //请求队列中的内容
            channel.queuePurge(RPC_QUEUE_NAME);

            channel.basicQos(1);

            System.out.println(" [x] Awaiting RPC requests");

            Object monitor = new Object();

            //收到请求消息后的回调函数
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                //设置发回响应的id，与请求id一致，这样客户端可以把该响应与它的请求对应
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();

                String response = "";

                try {
                    String message = new String(delivery.getBody(), "UTF-8");
                    int n = Integer.parseInt(message);

                    System.out.println(" [.] fib(" + message + ")");
                    //
                    response += fib(n);

                } catch (RuntimeException e) {
                    System.out.println(" [.] " + e.toString());
                } finally {

                    //发送消息到响应的队列
                    channel.basicPublish("", delivery.getProperties().getReplyTo(),
                            replyProps, response.getBytes("UTF-8"));
                    //手动确认
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    // RabbitMq consumer worker thread notifies the RPC server owner thread
                    synchronized (monitor) {
                        monitor.notify();
                    }
                }
            };

            //消费者开始接收消息，等待从rpc_queue接收请求消息，不自动确认。
            channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, (consumerTag -> { }));
            // Wait and be prepared to consume the message from RPC client.
            //等待并准备使用来自RPC客户端的消息。
            while (true) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}