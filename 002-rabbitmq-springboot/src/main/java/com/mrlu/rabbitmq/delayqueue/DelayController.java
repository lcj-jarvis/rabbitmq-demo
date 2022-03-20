package com.mrlu.rabbitmq.delayqueue;

import com.mrlu.rabbitmq.config.delayqueue.DelayQueueConfig;
import com.mrlu.rabbitmq.config.ttl.TtlQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author 简单de快乐
 * @date 2021-07-08 22:37
 *
 *  rabbitmq 安装在rabbit-server中
 * (1)进入rabbitmq的安装插件目录
 *    cd /usr/lib/rabbitmq/lib/rabbitmq_server-3.8.8/plugins
 * (2)通过xftp将
 *    E:\入门微服务\RabbitMQ\尚硅谷RabbitMQ课堂笔记\软件\rabbitmq_delayed_message_exchange-3.8.0.ez
 *    放到/usr/lib/rabbitmq/lib/rabbitmq_server-3.8.8/plugins目录下
 *（3）开启插件
 *    abbitmq-plugins enable rabbitmq_delayed_message_exchange
 *（4）重启rabbitmq的服务
 *   systemctl restart rabbitmq-server
 *（5）访问rabbitmq的客户端，就会发现多了一个x-delayed-message交换机的类型可以选择
 *
 */
@RestController
@Slf4j
@RequestMapping("/delay")
public class DelayController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 先发30s的
     * http://localhost:8080/delay/hello/30000
     * 再发3s的
     * http://localhost:8080/delay/hello1/3000
     * 最后就会发现3s的先被收到，30s的后被收到
     * @param message
     * @param delayTime
     */
    @GetMapping("/{message}/{delayTime}")
    public void sendDelayedMessage(@PathVariable("message") String message,
                                   @PathVariable("delayTime") Integer delayTime) {
        log.info("当前时间：{}，发送一条消息给两个TTL队列：{}", LocalDateTime.now().toString(), message);

        MessagePostProcessor messagePostProcessor = msg -> {
            //设置消息的延时时长,单位是ms
            msg.getMessageProperties().setDelay(delayTime);
            return msg;
        };

        rabbitTemplate.convertAndSend(DelayQueueConfig.DELAY_EXCHANGE_NAME,
                DelayQueueConfig.DELAY_ROUTING_KEY,
                "消息来自延迟队列：" + message, messagePostProcessor);
    }
}
