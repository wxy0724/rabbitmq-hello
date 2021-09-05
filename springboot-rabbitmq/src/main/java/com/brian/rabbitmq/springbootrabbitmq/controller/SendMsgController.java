package com.brian.rabbitmq.springbootrabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author brian
 * @date 2021/9/1
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间：{}，发送一条消息给两个TTL队列：{}", new Date().toString(), message);

        rabbitTemplate.convertAndSend("x", "XA", "消息来自ttl为10s的队列：" + message);
        rabbitTemplate.convertAndSend("x", "XB", "消息来自ttl为40s的队列：" + message);

    }

    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendExpirationMsg(@PathVariable String message, @PathVariable String ttlTime) {
        log.info("当前时间：{}，发送一条时长{}毫秒TTL消息给队列QC队列：{}", new Date().toString(),ttlTime,
                message);

        rabbitTemplate.convertAndSend("x", "XC", message, msg->{
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
        rabbitTemplate.convertAndSend("x", "XB", "消息来自ttl为40s的队列：" + message);

    }

}
