package com.mrlu.rabbitmq.workqueue.manualack;

import com.mrlu.rabbitmq.config.workqueue.WorkQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 简单de快乐
 * @date 2021-07-11 15:25
 *
 * 手动确认模式，加上以下配置
 * #消息确认模式：可选模式：none（不确认）、auto（自动确认）、manual（手动确认）
 * spring.rabbitmq.listener.simple.acknowledge-mode=manual
 * #通道的预取值
 * spring.rabbitmq.listener.simple.prefetch=1
 */
@Controller
@Slf4j
public class WorkQueueProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/manual/{message}/{count}")
    public void sendPrioryMessage(@PathVariable("message")String msg,
                                  @PathVariable("count")Integer count) {
        for (int i = 0; i < count; i++) {
            String message = msg + "-" + i;
            log.info("发送的消息{}", message);
            rabbitTemplate.convertAndSend("", WorkQueueConfig.MANUAL_QUEUE, message);

        }
    }
}
