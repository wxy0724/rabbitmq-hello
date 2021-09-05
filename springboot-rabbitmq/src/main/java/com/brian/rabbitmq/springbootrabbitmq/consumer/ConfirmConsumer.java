package com.brian.rabbitmq.springbootrabbitmq.consumer;

import com.brian.rabbitmq.springbootrabbitmq.config.ConfirmConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author brian
 * @date 2021/9/1
 */
@Slf4j
@Component
public class ConfirmConsumer {

    /**
     * 接收消息
     */
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveD(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("当前时间：{}，收到队列的消息：{}", new Date(), msg);
    }
}
