package com.mrlu.rabbitmq.config;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 简单de快乐
 * @date 2021-05-21 23:57
 *
 *   发布确认：
 *  1、消息发送到server，但是在server中找不到交换机，触发ConfirmCallback回调函数
 *  2、消息推送到server，找到了交换机，但是没有找到队列，会触发ConfirmCallback和ReturnsCallback回调函数
 *  3、消息推送到server，交换机和队列都找不到，触发ConfirmCallback回调函数
 *  4、消息推送成功，触发的是ConfirmCallback回调函数
 */
@Configuration
public class RabbitCallBackConfig {

    /**
     * 自定义RabbitTemplate，设置回调函数
     * @param configurer
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(RabbitTemplateConfigurer configurer, ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate();
        configurer.configure(template, connectionFactory);

        //回调函数1
        template.setConfirmCallback((correlationData, ack, cause) -> {
            System.out.println("ConfirmCallback:   相关数据："+correlationData);
            System.out.println("ConfirmCallback:   确认情况："+ack);
            System.out.println("ConfirmCallback:   原因："+cause);
        });

        //回调函数2
        template.setReturnsCallback(returned -> {
            System.out.println("ReturnsCallback 消息："+returned.getMessage());
            System.out.println("ReturnsCallback 回应码："+returned.getReplyCode());
            System.out.println("ReturnsCallback 回应消息："+returned.getReplyText());
            System.out.println("ReturnsCallback 交换机："+returned.getExchange());
            System.out.println("ReturnsCallback 路由键："+returned.getRoutingKey());
        });

        return template;
    }
}
