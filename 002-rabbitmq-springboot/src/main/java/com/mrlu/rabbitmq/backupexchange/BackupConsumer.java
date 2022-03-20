package com.mrlu.rabbitmq.backupexchange;

import com.mrlu.rabbitmq.config.backup.BackupConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 简单de快乐
 * @date 2021-07-09 23:09
 */
@Component
@Slf4j
public class BackupConsumer {

    @RabbitListener(queues = BackupConfig.BACKUP_QUEUE)
    public void receive(Message message) {
        log.info("备份交换机的备份消费者收到消息：{}", new String(message.getBody()));

    }
}
