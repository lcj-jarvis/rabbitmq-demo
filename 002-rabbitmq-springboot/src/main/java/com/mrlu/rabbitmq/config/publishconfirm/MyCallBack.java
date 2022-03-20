package com.mrlu.rabbitmq.config.publishconfirm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author 简单de快乐
 * @date 2021-07-09 21:41
 */
@Slf4j
//@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 构造方法后执行，注入RabbitTemplate中对应的属性
     */
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 交换机的确认回调方法
     * 1、发消息 交换机收到了 回调
     *   1.1 correlationData 保存回调消息的ID已经消息，由生成者传过来的
     *   1.2 交换机收到消息 ack = true
     *   1.3 cause null
     * 2、发消息 交换机接收失败了 回调
     *   2.1 correlationData 保存回调消息的ID已经消息，由生成者传过来的
     *   2.2 交换机收到消息 ack = false
     *   2.3 cause 失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData == null ? null : correlationData.getId();
        if (ack) {
            log.info("交换机收到{}的消息", id);
        } else {
            log.error("交换机未收到{}的消息，原因是{}", id, cause);

        }
    }

    /**
     * 交换机为发送消息给队列时回调
     * @param returned
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        String routingKey = returned.getRoutingKey();
        String exchange = returned.getExchange();
        Message message = returned.getMessage();
        String replyText = returned.getReplyText();
        int replyCode = returned.getReplyCode();
        log.info("交换机{}退回routingKey{}的消息{}，原因是{}，错误状态码是{}", exchange, routingKey,
                message.getBody(), replyText, replyCode);
    }
}
