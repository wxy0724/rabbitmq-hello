package com.brian.rabbitmq.springbootrabbitmq.controller;

import com.brian.rabbitmq.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author brian
 * @date 2021/9/4
 */
@Slf4j
@RestController
@RequestMapping("/confirm")
public class ProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendEMessage/{message}")
    public void sendEMessage(@PathVariable String message) {
        //log.info("发送消息，内容为{}", message);
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("1");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY, message, correlationData);
        log.info("发送消息内容：{}", message + "key1");

        CorrelationData correlationData2 = new CorrelationData();
        correlationData2.setId("2");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY + 2, message, correlationData);
        log.info("发送消息内容：{}", message + "key12");

    }

}
