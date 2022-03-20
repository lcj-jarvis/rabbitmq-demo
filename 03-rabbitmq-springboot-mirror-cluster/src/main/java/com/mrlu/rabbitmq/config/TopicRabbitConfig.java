package com.mrlu.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 简单de快乐
 * @date 2021-05-21 21:39
 */
@Configuration
public class TopicRabbitConfig {

    @Bean
    public Queue firstQueue(){
        return new Queue("03-rabbitmq-springboot-mirror-cluster-firstQueue");
    }

    @Bean
    public Queue secondQueue(){
        return new Queue("03-rabbitmq-springboot-mirror-cluster-secondQueue");
    }

    @Bean
    public Queue thirdQueue(){
        return new Queue("03-rabbitmq-springboot-mirror-cluster-thirdQueue");
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("TopicExchange",true,false);
    }

    /**
     * 绑定队列和交换机
     * @return
     */
    @Bean
    public Binding bindingExchangeForFirstQueue(){
       return BindingBuilder.bind(firstQueue())
               .to(topicExchange())
               .with("*.order.*");
    }


    @Bean
    public Binding bindingExchangeForSecondQueue(){
       return BindingBuilder.bind(secondQueue())
               .to(topicExchange())
               .with("*.order.#");
    }

    @Bean
    public Binding bindingExchangeForThirdQueue(){
       return BindingBuilder.bind(thirdQueue())
               .to(topicExchange())
               .with("*.add");
    }
}
