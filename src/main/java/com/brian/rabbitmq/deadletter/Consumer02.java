package com.brian.rabbitmq.deadletter;

import com.brian.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author brian
 * @date 2021/8/29
 */
public class Consumer02 {

    public static final String DEAD_QUEUE_NAME = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("等待接收消息，把接收到的消息打印到屏幕上。。。。。。");
        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer02控制台打印接收到的消息："
                    + new String(message.getBody(),
                    StandardCharsets.UTF_8));
        };
        // 消费者取消消息时回调接口
        channel.basicConsume(DEAD_QUEUE_NAME, true, deliverCallback,
                consumerTag -> {
                });
    }


}
