package com.mrlu.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 简单de快乐
 * @date 2021-05-21 20:57
 */
@Configuration
public class DirectRabbitConfig {

    /**
     * 队列 TestDirectQueue
     * @return
     */
    @Bean
    public Queue TestDirectQueue1(){
        //public Queue(String name, boolean durable, boolean exclusive, boolean autoDelete) {}
        /**
         * name:表示该队列的名称
         * durable：表示是否持久化
         * exclusive：表示独占队列
         * autoDelete：表示消息消费完后是否删除队列
         */
        return new Queue("02-rabbitmq-spring-TestDirectQueue",
                true,false,false);
    }

    @Bean
    public Queue TestDirectQueue2(){
        //public Queue(String name, boolean durable, boolean exclusive, boolean autoDelete) {}
        /**
         * name:表示该队列的名称
         * durable：表示是否持久化
         * exclusive：表示独占队列
         * autoDelete：表示消息消费完后是否删除队列
         * */

        return new Queue("02-rabbitmq-spring-NoneDirectQueue",
                true,false,false);
    }

    /**
     * Direct交换机 起名为TestDirectExchange
     * @return
     */
    @Bean
    public DirectExchange TestDirectExchange(){
        //public DirectExchange(String name, boolean durable, boolean autoDelete) {}
        /**
         * name：交换机的名称
         * durable：是否持久化
         * autoDelete：是否自动删除
         */
        return new DirectExchange("TestDirectExchange",true,false);
    }

    /**
     * 将队列和交换机绑定在一起,并设置路由的key为TestDirectRouting
     * @return
     */
    @Bean
    public Binding bindingDirectAndQueue1a(){
       return BindingBuilder.bind(TestDirectQueue1()).
                to(TestDirectExchange()).with("TestDirectRouting1");

    }
    @Bean
    public Binding bindingDirectAndQueue1b(){
        return BindingBuilder.bind(TestDirectQueue1()).
                to(TestDirectExchange()).with("TestDirectRouting2");

    }

    @Bean
    public Binding bindingDirectQueue2(){
        return BindingBuilder.bind(TestDirectQueue2()).
                to(TestDirectExchange()).with("TestDirectRouting2");

    }

    @Bean
    public DirectExchange lonelyDirectExchange(){
        //public DirectExchange(String name, boolean durable, boolean autoDelete) {}
        /**
         * name：交换机的名称
         * durable：是否持久化
         * autoDelete：是否自动删除
         */
        return new DirectExchange("lonelyDirectExchange",true,false);
    }
}
