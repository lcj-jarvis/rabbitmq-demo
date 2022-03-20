package com.mrlu.rabbitmq.helloworld;

import com.mrlu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author 简单de快乐
 * @date 2021-05-19 18:14
 *
 * 直连模型：点对点的连接。
 * 注意：生成者和消费者所用的队列要一致（参数那些）
 *
 * 缺点：当消息处理比较耗时的时候，可能生产消息的速度会远远大于消息的消费速度。
 *      长此以往，消息就会堆积越来越多，无法及时处理
 */
public class Producer01 {

    /**
     * 生成者发布消息
     * @param args
     */
    public static void main(String[] args) throws Exception{
        //1、创建连接mq的连接工程对象
        //ConnectionFactory connectionFactory = new ConnectionFactory();
        //2、设置连接rabbitmq的主机ip地址
        //connectionFactory.setHost("192.168.187.100");
        //3、设置连接的端口号
        //connectionFactory.setPort(5672);
        //2、3步相等于设置mq服务器所在的位置

        //4、设置连接到哪个虚拟主机
        //connectionFactory.setVirtualHost("/01-rabbitmq");
        //5、设置访问虚拟主机的用户名和密码
        //connectionFactory.setUsername("lu");
        //connectionFactory.setPassword("12345");

        Connection connection = null;
        Channel channel = null;

        //6、获取连接对象
        //connection = connectionFactory.newConnection();
        connection = RabbitMqUtils.getConnection("192.168.187.100",5672,"/01-rabbitmq","lu","12345");
        //7、获取连接中的通道
        channel = connection.createChannel();

        //8、通道绑定消息队列
        //参数1：队列的名称，如果队列不存在就会自动创建

        //参数2：用来定义队列特性是否要持久化
        //      true 表示持久化，此时如果RabbitMQ服务重启的话，队列不会丢失，当时原本消息队列里的消息会丢失
        //           如果查看RabbitMQ的界面的Queues,会发现队列的Features有个大写字母D

        //      false 表示不持久化。此时如果RabbitMQ服务重启的话，队列就会丢失，即原本的队列就会不存在
        //      注意：持久化队列，不代表消息也是持久化的

        //参数3：exclusive 是否独占队列
        //        true独占队列，表示只有该连接和通道能访问队列，其他的通道和连接不能访问.
        //          如果查看RabbitMQ的界面的Queues,会发现队列的Features有个大写字母AD
        //        false表示不独占

        //   （1）当exclusive = true则设置队列为排他的。如果一个队列被声明为排他队列，
        //       该队列仅对首次声明它的连接（Connection）可见，是该Connection私有的，
        //       类似于加锁，并在连接断开connection.close()时自动删除 ；
        //   （2）当exclusive = false则设置队列为非排他的，此时不同连接（Connection）的
        //       管道Channel可以使用该队列 ；
        //     排他队列是基于连接(Connection) 可见的，同个连接（Connection）的不同
        //     管道 (Channel) 是可以同时访问同一连接创建的排他队列 。
        //     其他连接是访问不了的 ，强制访问将报错：
        //     com.rabbitmq.client.ShutdownSignalException: channel error; protocol method:
        //     #method<channel.close>(reply-code=405, reply-text=RESOURCE_LOCKED -
        //     cannot obtain exclusive access to locked queue 'hello-testExclusice' in vhost '/'.
        //
        //    "首次" 是指如果某个连接（Connection）已经声明了排他队列，其他连接是不允许建立同名的排他队列的。
        //    这个与普通队列不同：即使该队列是持久化的(durable = true)，一旦连接关闭或者客户端退出，
        //    该排他队列都会被自动删除，这种队列适用于一个客户端同时发送和读取消息的应用场景。

        //参数4：autoDelete:是否在消费者消费完队列中的消息且消费者与队列断开连接后自动删除队列。
        //      true表示自动删除 ，false表示否

        //参数5：额外添加的参数 死信队列的参数等
        // channel.queueDeclare("01-rabbitmq-hello", false, false, false, null);

        //即使绑定的是01-rabbitmq-hello01队列，但是下面发送消息的队列是01-rabbitmq-hello
        //此时消息还是发送到01-rabbitmq-hello队列中。如果想向01-rabbitmq-hello01队列发送消息。
        // 就要将basicPublish方法里的队列改为"01-rabbitmq-hello01"
        //由此可见：同一个通道可以向不同的队列发送消息【注意】
        channel.queueDeclare("01-rabbitmq-hello01", true, false, true, null);

        //9、发布消息
        //参数1：交换机名称 参数2：队列名称 参数3：传递消息的特外设置 参数4：消息的具体内容
        //channel.basicPublish("", "01-rabbitmq-hello", null, "hello rabbitmq".getBytes());

        //MessageProperties.PERSISTENT_TEXT_PLAIN表示当RabbitMQ服务重启时，队列中的消息也会持久化保存。重启成功后，也队列中的消息也不会丢失。
        //在RabbitMQ正常启动中，我们的消息是在队列中的，是在内存中的。
        // 当rabbitmq重启的过程中，会把消息和队列持久化到硬盘中。再次启动时，会把队列中的消息进行恢复。
        channel.basicPublish("", "01-rabbitmq-hello01", MessageProperties.PERSISTENT_TEXT_PLAIN,
                "hello rabbitmq".getBytes());

        //注意：先关闭通道，再关闭连接.
        //其实 channel实现了自动 close 接口，自动关闭不需要显示关闭
        channel.close();
        connection.close();
    }
}
