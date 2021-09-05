package com.brian.rabbitmq.springbootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 交换机确认回调接口
 * @author brian
 * @date 2021/9/4
 */
@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback,
        RabbitTemplate.ReturnsCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 交换机确认回调方法
     * 1 发消息 交换机接收到了 回调
     * 参数描述
     *   correlationData 保存回调消息的ID以及相关信息
     *   交换机收到消息 true
     *   null
     * 2 发消息 交换机接收失败了
     *  correlationData 保存回调消息的ID以及相关信息
     *  交换机收到消息 true
     *  cause 失败的原因
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData.getId() == null ? "" : correlationData.getId();
        if (ack) {
            log.info("交换机已经收到ID为：{}的消息", id);
        } else {
            log.info("交换机未收到ID为：{}的消息，原因：{}", id, cause);
        }
    }

    /**
     * 通过设置mandatory参数可以在当消息传递过程中不可达目的地时将消息返回给生产者
     * 只有不可达目的地的时候才尽兴回退
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    //@Override
    //public void returnedMessage(Message message, int replyCode, String replyText,
    //                            String exchange, String routingKey) {
    //    log.error("消息{}，被交换机{}退回，退回原因：{}，路由key：{}",
    //            new String(message.getBody()), exchange, replyText, routingKey);
    //}

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        Message message = returned.getMessage();
        String exchange = returned.getExchange();
        String routingKey = returned.getRoutingKey();
        String replyText = returned.getReplyText();
        log.error("消息{}，被交换机{}退回，退回原因：{}，路由key：{}",
                new String(message.getBody()), exchange, replyText, routingKey);
    }
}
