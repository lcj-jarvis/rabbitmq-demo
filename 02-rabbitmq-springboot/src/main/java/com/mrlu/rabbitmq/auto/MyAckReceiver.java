package com.mrlu.rabbitmq.auto;

import com.rabbitmq.client.Channel;
import com.rabbitmq.tools.json.JSONUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author 简单de快乐
 * @date 2021-05-22 14:41
 *
 * 手动确认的消费者
 */
@Component
public class MyAckReceiver implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {

            System.out.println(message.toString());
            //太多if的话，通过反射优化
            if ("02-rabbitmq-springboot-fanout-firstQueue"
                    .equals(message.getMessageProperties().getConsumerQueue())){
                System.out.println("来自===》"+message.getMessageProperties().getConsumerQueue());
                //后续的业务处理
            }

            if ("02-rabbitmq-springboot-fanout-secondQueue"
                    .equals(message.getMessageProperties().getConsumerQueue())){
                System.out.println("来自===》"+message.getMessageProperties().getConsumerQueue());
                //后续的业务处理
            }
            //第二个参数，手动确认可以批量处理，当该参数为true时，
            // 则可以进行一次性确认delivery_Tag小于等于传入值的所有消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        } catch (Exception e) {
            //第二个参数为设置重新入列
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
            e.printStackTrace();
        }
    }
}
