package com.mrlu.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 简单de快乐
 * @date 2021-05-21 22:53
 */
@Configuration
public class FanoutRabbitConfig {

    @Bean
    public Queue fanoutFirstQueue(){
        return new Queue("02-rabbitmq-springboot-fanout-firstQueue");
    }

    @Bean
    public Queue fanoutSecondQueue(){
        return new Queue("02-rabbitmq-springboot-fanout-secondQueue");
    }

    @Bean
    public Queue fanoutThirdQueue(){
        return new Queue("02-rabbitmq-springboot-fanout-thirdQueue");
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("fanoutExchange",true,false);
    }

    @Bean
    public Binding bindingExchangeForFanoutFirstQueue(){
        return BindingBuilder.bind(fanoutFirstQueue()).to(fanoutExchange());
    }

    @Bean
    public Binding bindingExchangeForFanoutSecondQueue(){
        return BindingBuilder.bind(fanoutSecondQueue()).to(fanoutExchange());
    }

    @Bean
    public Binding bindingExchangeForFanoutThirdQueue(){
        return BindingBuilder.bind(fanoutThirdQueue()).to(fanoutExchange());
    }
}
