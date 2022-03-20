package com.mrlu.rabbitmq.config.ttl;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 简单de快乐
 * @date 2021-07-08 20:46
 *
 * TTL队列配置类，根据TTL队列实战图进行配置。
 *
 * 在队列中配置过期时间有什么缺点呢？
 *   如果这样使用的话，岂不是每增加一个新的时间需求，就要新增一个队列，这里只有 10S 和 40S
 *   两个时间选项，如果需要一个小时后处理，那么就需要增加 TTL 为一个小时的队列。
 *
 * 所以要在生成者中设置消息的TTL
 */
@Configuration
public class TtlQueueConfig {

    /**
     * 普通交换机的名称
     */
    public static final String X_EXCHANGE = "X";

    /**
     * 死信交换机的名称
     */
    public static final String Y_EXCHANGE = "Y";

    /**
     * 普通队列的名称
     */
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String QUEUE_C = "QC";

    /**
     * 死信队列的名称
     */
    public static final String DEAD_LETTER_QUEUE = "QD";

    //声明交换机
    @Bean
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    //声明死信交换机
    @Bean
    public DirectExchange yExchange() {
        //ExchangeBuilder类来创建也行
        return new DirectExchange(Y_EXCHANGE);
    }

    //声明普通队列TTL为5s
    @Bean
    public Queue queueA() {
        //方式一
        Map<String, Object> map = new HashMap<>(16);
        //设置死信交换机
        map.put("x-dead-letter-exchange", Y_EXCHANGE);
        //设置死信routingKey。这里routingKey是死信队列连接死信交换机的
        map.put("x-dead-letter-routing-key", "dead");
        //设置TTL 单位是ms
        map.put("x-message-ttl", 5000);
        return QueueBuilder.durable(QUEUE_A).withArguments(map).build();

        //方式二
        /*return QueueBuilder.durable(QUEUE_A).ttl(10000)
                .deadLetterExchange(Y_EXCHANGE)
                .deadLetterRoutingKey("dead").build();*/
    }

    //声明普通队列TTL为10s
    @Bean
    public Queue queueB() {
        //方式一
        Map<String, Object> map = new HashMap<>(16);
        //设置死信交换机
        map.put("x-dead-letter-exchange", Y_EXCHANGE);
        //设置死信routingKey。这里routingKey是死信队列连接死信交换机的
        map.put("x-dead-letter-routing-key", "dead");
        //设置TTL 单位是ms
        map.put("x-message-ttl", 10000);
        return QueueBuilder.durable(QUEUE_B).withArguments(map).build();

        //方式二
        /*return QueueBuilder.durable(QUEUE_B).ttl(10000)
                .deadLetterExchange(Y_EXCHANGE)
                .deadLetterRoutingKey("dead").build();*/
    }

    @Bean
    public Queue queueC() {
        //方式一
        /*Map<String, Object> map = new HashMap<>(16);
        //设置死信交换机
        map.put("x-dead-letter-exchange", Y_EXCHANGE);
        //设置死信routingKey。这里routingKey是死信队列连接死信交换机的
        map.put("x-dead-letter-routing-key", "dead");
        //设置TTL 单位是ms
        map.put("x-message-ttl", 10000);
        return QueueBuilder.durable(QUEUE_B).withArguments(map).build();*/

        //方式二
        return QueueBuilder.durable(QUEUE_C)
                .deadLetterExchange(Y_EXCHANGE)
                .deadLetterRoutingKey("dead").build();
    }

    //声明死信队列
    @Bean
    public Queue queueD() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    //绑定队列和交换机
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    //绑定队列和交换机
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    //绑定队列和交换机
    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }

    //绑定队列和交换机
    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("dead");
    }

}
