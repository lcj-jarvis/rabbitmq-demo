package com.mrlu.rabbitmq.config.backup;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 简单de快乐
 * @date 2021-07-09 22:05
 *
 * 有时候，我们并不知道该如何处理这些无法路由的消息，最多打个日志，然后触发报警，再来手动处理。
 * 而通过日志来处理这些无法路由的消息是很不优雅的做法，特别是当生产者所在的服务有多台机器的时候，
 * 手动复制日志会更加麻烦而且容易出错。
 *
 * 解决办法：
 * 在 RabbitMQ 中，有一种备份交换机的机制存在，可以很好的应对这个问题。什么是备份交换机呢？备份
 * 交换机可以理解为 RabbitMQ 中交换机的“备胎”，当我们为某一个交换机声明一个对应的备份交换机时，
 * 就是为它创建一个备胎，当交换机接收到一条不可路由消息时，将会把这条消息转发到备份交换机中，由
 * 备份交换机来进行转发和处理，通常备份交换机的类型为 Fanout ，这样就能把所有消息都投递到与其绑
 * 定的队列中，然后我们在备份交换机下绑定一个队列，这样所有那些原交换机无法被路由的消息，就会都
 * 进入这个队列了。当然，我们还可以建立一个报警队列，用独立的消费者来进行监测和报警。
 *
 * 参考备用交换机实战图进行配置
 *
 * mandatory 参数与备份交换机可以一起使用的时候，如果两者同时开启，消息究竟何去何从？谁优先级高，
 * 经过测试显示答案是：备份交换机优先级高 【注意】
 */
@Configuration
public class BackupConfig {

    /**
     * 发布确认交换机
     */
    public static final String CONFIRM_EXCHANGE = "confirm_exchange";

    /**
     * 备用交换机
     */
    public static final String BACKUP_EXCHANGE = "backup_exchange";

    /**
     * 发布确认队列
     */
    public static final String CONFIRM_QUEUE = "confirm_queue";

    /**
     * 备用交换机的队列
     */
    public static final String BACKUP_QUEUE = "backup_queue";
    public static final String WARNING_QUEUE = "warning_queue";


    /**
     * routingKey
     */
    public static final String CONFIRM_ROUTING_KEY = "confirm_routing_key";

    @Bean
    public DirectExchange confirmExchange() {
        //配置交换机将无法路由的消息投递到备份交换机
        //方式一
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE).alternate(BACKUP_EXCHANGE).build();

        //方式二
        //alternate-exchange
        /*return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE).withArgument("alternate-exchange", BACKUP_EXCHANGE)
                .build();*/
    }

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE).build();
    }

    @Bean
    public Binding confirmQueueBindingConfirmExchange() {
        return BindingBuilder.bind(confirmQueue()).to(confirmExchange()).with(CONFIRM_ROUTING_KEY);
    }

    /**
     * 声明备用交换机
     * @return
     */
    @Bean
    public FanoutExchange backupExchange() {
        return ExchangeBuilder.fanoutExchange(BACKUP_EXCHANGE).build();
    }

    @Bean
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE).build();
    }

    @Bean
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE).build();
    }

    /**
     * 绑定备份交换机和对应的队列
     * @return
     */
    @Bean
    public Binding backupQueueBinding() {
        return BindingBuilder.bind(backupQueue()).to(backupExchange());
    }

    @Bean
    public Binding warningQueueBinding() {
        return BindingBuilder.bind(warningQueue()).to(backupExchange());
    }
}
