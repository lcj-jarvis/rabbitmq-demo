package com.mrlu.rabbitmq.priorityqueue;

import com.mrlu.rabbitmq.config.priorityqueue.PriorityQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Random;
import java.util.UUID;

/**
 * @author 简单de快乐
 * @date 2021-07-09 23:50
 *
 * 测试的时候，先把消费者的的注解注释掉，然后运行，发送消息后，再把消费者的注入容器，重新启动容器。
 * 就可以看到消息按照优先级被接收到了
 */
@Controller
@Slf4j
public class PriorityQueueProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/priority/{message}/{count}")
    public void sendPrioryMessage(@PathVariable("message")String msg,
                                  @PathVariable("count")Integer count) {
        for (int i = 0; i < count; i++) {
            Random priorityBuilder = new Random();
            int priority = priorityBuilder.nextInt(11);
            String message = "消息" + msg + UUID.randomUUID().toString() + "的优先级为：" + priority;
            //设置消息的优先级
            MessagePostProcessor msgConfig = (msgConf) -> {
                msgConf.getMessageProperties().setPriority(priority);
                return msgConf;
            };
            log.info("发送的消息{}", message);
            rabbitTemplate.convertAndSend(PriorityQueueConfig.PRIORITY_EXCHANGE,
                    PriorityQueueConfig.PRIORITY_ROUTING_KEY,
                    message, msgConfig);
        }
    }
}
