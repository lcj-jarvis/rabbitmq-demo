package com.mrlu.rabbbitmq.helloworld;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 简单de快乐
 * @date 2021-05-21 16:11
 *
 * 测试时将@Component注解放开
 */
//@Component
public class RabbitMqProducer {

    //注入RabbitTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * @Scheduled注解 表示设置一个定时任务，initialDelay = 500，表示springboot容器刚启动的时候，
     * 等到500ms后，才会执行。
     * fixedDelay = 1000，表示第一次执行完成后，之后每隔1000ms执行一次。
     */
    @Scheduled(fixedDelay = 1000,initialDelay = 500)
    public void send(){
        //第一个参数routingkey，第二个为要发送的数据
        /**
         * 在老师的生产端没有指定交换机只有routingKey和Object，
         * 也就是说这个消费方产生hello队列，放在默认的交换机(AMQP default)上。
         * 而默认的交换机有一个特点，只要你的routerKey与这个交换机中有同名的队列，他就会自动路由上。
         * 生产端routingKey 叫hello ，消费端生产hello队列。他们就路由上了
         */
        rabbitTemplate.convertAndSend("hello","hello world");
    }
}
