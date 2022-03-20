package com.mrlu.rabbitmq.ttlqueue;

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
 * @date 2021-07-08 21:22
 *
 * 如果不设置 TTL，表示消息永远不会过期，
 * 如果将 TTL 设置为 0，则表示除非此时可以直接
 * 投递该消息到消费者，否则该消息将会被丢弃。
 */
@RestController
@RequestMapping(value = "/ttl")
@Slf4j
public class TtlController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{message}")
    public void sendMessage(@PathVariable("message") String message) {
        log.info("当前时间：{}，发送一条消息给两个TTL队列：{}",LocalDateTime.now().toString(), message);

        rabbitTemplate.convertAndSend(TtlQueueConfig.X_EXCHANGE,"XA", "消息来自ttl为5s的队列："+message);
        rabbitTemplate.convertAndSend(TtlQueueConfig.X_EXCHANGE,"XB", "消息来自ttl为10s的队列："+message);
    }

    /**
     * 生成者设置时间，解决如果想配置多个不同时间的要在配置类中配置多个队列的问题。
     * 但是这样也会存在一个问题：
     *  先发一个延时10s的消息
     *  http://localhost:8080/ttl/sendExpirationMsg/hello1/10000
     *  再发一个延时2s的消息
     *  http://localhost:8080/ttl/sendExpirationMsg/hello1/2000
     *
     *  发现10s过后，两条消息同时发送。
     *  因为如果在消息属性上设置TTL的方式，消息可能并不会按时“死亡”。
     *  因为RabbitMQ只会检查第一个消息是否过期，如果过期则丢到死信队列。
     *  如果第一个消息的延时时长很长，而第二个消息的延时时长很短，在第一个消息过期时候，
     *  实际上第二个消息也已经过期了。RabbitMQ并不会优先执行过期时间段的。
     *  是按照发送的顺序执行，所以就会出现以上问题。
     *
     *  要解决这个问题，就要使用rabbitmq插件，见使用插件完成延时队列，解决这个问题。
     * @param message
     * @param expiration
     */
    @GetMapping("/sendExpirationMsg/{message}/{ttl}")
    public void sendMessage(@PathVariable("message") String message,
                            @PathVariable("ttl") String expiration) {
        log.info("当前时间：{}，发送一条消息给两个TTL队列：{}",LocalDateTime.now().toString(), message);

        //设置消息的延时时长
        MessagePostProcessor messagePostProcessor = msg -> {
            msg.getMessageProperties().setExpiration(expiration);
            return msg;
        };

        rabbitTemplate.convertAndSend(TtlQueueConfig.X_EXCHANGE,"XC",
                "消息来自ttl的队列：" + message, messagePostProcessor);
    }
}
