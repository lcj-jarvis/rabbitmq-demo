package com.mrlu.rabbitmq.rpc;

/**
 * @author 简单de快乐
 * @date 2021-05-23 0:04
 *
 *
 * RPC客户端
 *
 */
import com.mrlu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

public class RPCClient implements AutoCloseable {

    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue";

    public RPCClient() throws IOException, TimeoutException {
        connection = RabbitMqUtils.getConnection("192.168.187.100", 5672,
                "/01-rabbitmq", "lu", "12345");
        channel = connection.createChannel();
    }

    public static void main(String[] argv) {

        try (RPCClient fibonacciRpc = new RPCClient()) {
            for (int i = 0; i < 32; i++) {
                String i_str = Integer.toString(i);
                System.out.println(" [x] Requesting fib(" + i_str + ")");
                //获取斐波那契数列的结果
                String response = fibonacciRpc.call(i_str);
                System.out.println(" [.] Got '" + response + "'");
            }
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String call(String message) throws IOException, InterruptedException {
        //生成关联id
        final String corrId = UUID.randomUUID().toString();
        //生成响应队列的名字
        String replyQueueName = channel.queueDeclare().getQueue();

        //设置两个参数
        //1、请求和响应的关联id
        //2、传递响应数据的队列
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        //requestQueueName为请求队列的名称。向rpc_queue队列中发送请求数据，请求第n个斐波那契数
        channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));

        //用来保存结果的阻塞队列，取数据时，没有数据会暂停等待
        /**
         * 主线程发送调用信息。
         * basicConsume的DeliverCallback相当于另外起一个线程等待接收的结果。
         * 主线程需要计算结果时，要取结果。
         * 线程之间传递数据，使用阻塞队列BlockingQueue
         */
        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
        String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            //Called when a <code><b>basic.deliver</b></code> is received for this consumer.
            //接收响应的回调
            //如果相应的关联Id和请求的关联Id相同，我们就处理这个响应的数据
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                //将响应的数据，放入到阻塞队列中
                response.offer(new String(delivery.getBody(), "UTF-8"));
            }
        }, consumerTag -> {
        });

        //从阻塞队列中,取出结果
        String result = response.take();
        //取消一个消费者
        channel.basicCancel(ctag);
        return result;
    }

    @Override
    public void close() throws IOException {
        connection.close();
    }
}
