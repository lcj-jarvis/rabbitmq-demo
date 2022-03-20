package com.mrlu.rabbitmq.publishconfirms;

import com.mrlu.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;

import java.time.Duration;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;


/**
 * @author 简单de快乐
 * @date 2021-05-22 21:48
 *
 * 发布确认:
 * 生成者将信道设置为确认模式，所有在信道上发布的消息都会被指派一个唯一的ID（从1开始），
 * 一旦消息被投递到所有匹配的队列，broker就会发送一个“确认”给生产者（包含消息的唯一ID），
 * 然后生成者便知道消息已经正常到达目的队列。这就是发布确认。
 *
 * 如果加上队列和消息的持久化，就可以做到避免数据的丢失。
 *
 */
public class PublishConfirms {

    //发布1000条消息
    static final int MESSAGE_COUNT = 1_000;

    static Connection createConnection() throws Exception {
        return RabbitMqUtils.getConnection("192.168.187.100", 5672,
                "/", "admin", "123");
    }

    public static void main(String[] args) throws Exception {
        //1092ms
        publishMessagesIndividually();

        //332 ms
        publishMessagesInBatch();

        //104 ms
        handlePublishConfirmsAsynchronously();
    }

    /**
     * 单独发布消息，同步等待确认：简单，但吞吐量非常有限。
     *
     * 这种技术非常简单，但也有一个主要缺点：它会显著减慢发布速度，
     * 因为消息的确认会阻止所有后续消息的发布。这种方法不会提供每秒超过几百条已发布消息的吞吐量。
     * 然而，对于某些应用程序来说，这已经足够好了。
     */
    static void publishMessagesIndividually() throws Exception {
        try (Connection connection = createConnection()) {
            Channel ch = connection.createChannel();

            //创建队列
            String queue = "individually";
            ch.queueDeclare(queue, false, false, true, null);

            //将通道设置为发布确认的级别（即开启发布确认）
            ch.confirmSelect();
            //返回正在运行的Java虚拟机高分辨率时间源的当前值，以纳秒为单位。相当于获取当前系统的时间
            long start = System.nanoTime();
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String body = String.valueOf("消息："+ i);
                ch.basicPublish("", queue, null, body.getBytes());

                /**
                 * Channel的waitForConfirmsOrDie（long）方法等待消息的确认。
                 * 消息一经确认，该方法即返回。如果消息在超时时间内未被确认，
                 * 或者消息未被确认（意味着代理由于某种原因无法处理它），则该方法将引发异常。
                 * 异常处理通常包括记录错误消息和/或重试发送消息。
                 */
                //ch.waitForConfirmsOrDie(5_000);
                boolean confirms = ch.waitForConfirms();
                if (confirms) {
                    System.out.println(body + "发送成功");
                }
            }
            //返回正在运行的Java虚拟机高分辨率时间源的当前值，以纳秒为单位。相当于获取当前系统的时间
            long end = System.nanoTime();
            System.out.format("Published %,d messages individually in %,d ms%n",
                    MESSAGE_COUNT, Duration.ofNanos(end - start).toMillis());
        }
    }

    /**
     * 批量发布消息，同步等待一批消息的确认：简单、合理的吞吐量，但很难解释什么时候出错。
     * 等待一批消息被确认大大提高了吞吐量，而不是等待单个消息的确认
     * （对于远程RabbitMQ节点，最多可提高20-30倍）。
     * 一个缺点是，我们不知道在失败的情况下到底出了什么问题，因此我们可能必须在内存中保留一整批
     * 内容来记录有意义的内容或重新发布消息。而且这个解决方案仍然是同步的，所以它阻止了消息的发布。
     */
    static void publishMessagesInBatch() throws Exception {
        try (Connection connection = createConnection()) {
            Channel ch = connection.createChannel();

            String queue = "Batch";
            ch.queueDeclare(queue, false, false, true, null);

            //开启发布确认
            ch.confirmSelect();

            int batchSize = 100;
            long start = System.nanoTime();
            for (int i = 1; i <= 1000; i++) {
                String body = String.valueOf("消息："+ i);
                ch.basicPublish("", queue, null, body.getBytes());

                if (i % batchSize == 0) {
                    //100条确认一次
                    ch.waitForConfirms();
                }
            }
            long end = System.nanoTime();
            System.out.format("Published %,d messages in batch in %,d ms%n",
                    MESSAGE_COUNT, Duration.ofNanos(end - start).toMillis());
        }
    }

    /**
     * 异步处理：最佳的性能和资源利用率，在发生错误时有良好的控制能力，但可以参与正确的实现。
     * 以下程序有三个线程进行通信,所以要用ConcurrentSkipListMap并发安全的map
     */
    static void handlePublishConfirmsAsynchronously() throws Exception {
        try (Connection connection = createConnection()) {
            Channel ch = connection.createChannel();

            String queue = "Asynchronously";
            ch.queueDeclare(queue, false, false, true, null);

            //将通道设置为发布确认的级别
            ch.confirmSelect();

            /**
             * 线程安全有序的一个哈希表，适合于高并发的情景
             * 保存的元素按照key排序，key保存消息序号，value保存消息的内容
             * 1、轻松将序号和消息进行关联
             * 2、轻松批量删除条目（内容），只要有序号
             * 3、支持高并发（多线程）
             */
            ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

            /**
             *
             * 序列号sequenceNumber：消息的编号。
             * 多重multiple：这是一个布尔值。如果为false，则只确认一条消息/nack-ed；
             *            如果为true，则确认通道中序列号较低或相等的所有消息/nack-ed。
             */
            //消息成功确认后的回调函数，异步执行。可以理解为有另一个线程执行
            ConfirmCallback cleanOutstandingConfirms = (sequenceNumber, multiple) -> {
                 if (multiple) {
                     //headMap(sequenceNumber)返回信道中第一个开始被确认成功的消息
                     ConcurrentNavigableMap<Long, String> begin = outstandingConfirms.headMap(sequenceNumber);
                     begin.clear();
                 }else {
                     outstandingConfirms.remove(sequenceNumber);
                 }
            };

            //消息确认失败后的回调函数，异步执行。可以理解为有另一个线程执行
            ConfirmCallback nackCallback = (sequenceNumber, multiple) -> {
                String message = outstandingConfirms.get(sequenceNumber);
                System.out.println(message + "确认失败");

                //失败也要清除map中的数据
                cleanOutstandingConfirms.handle(sequenceNumber, multiple);
            };

            //参数1为确认消息后的回调，参数2为未确认消息的回调
            ch.addConfirmListener(cleanOutstandingConfirms, nackCallback);

            long start = System.nanoTime();
            //只管发布即可，发布消息又是一个独立的线程
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String body = String.valueOf(i);
                //在发布之前，可以通过通道的getNextPublishSeqNo（）返回下一个要发布的消息的序列号：
                //ch.getNextPublishSeqNo()
                outstandingConfirms.put(ch.getNextPublishSeqNo(), body);
                ch.basicPublish("", queue, null, body.getBytes());
            }

            long end = System.nanoTime();
            System.out.format("Published %,d messages and handled confirms asynchronously in %,d ms%n",
                    MESSAGE_COUNT, Duration.ofNanos(end - start).toMillis());
        }
    }

}
