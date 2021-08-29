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
public class Consumer01 {

    public static final String NORMAL_EXCHANGE_NAME = "normal_exchange";
    public static final String DEAD_EXCHANGE_NAME = "dead_exchange";

    public static final String NORMAL_QUEUE_NAME = "normal_queue";
    public static final String DEAD_QUEUE_NAME = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 声明普通队列
        Map<String, Object> arguments = new HashMap<>();
        // 过期时间
        //arguments.put("x-message-ttl", 10000);
        // 正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        // 设置死信routingkey
        arguments.put("x-dead-letter-routing-key", "lisi");
        channel.queueDeclare(NORMAL_QUEUE_NAME, false, false,
                false, arguments);
        // 声明死信队列
        channel.queueDeclare(DEAD_QUEUE_NAME, false, false,
                false, null);
        channel.queueBind(NORMAL_QUEUE_NAME, NORMAL_EXCHANGE_NAME, "zhangsan");
        channel.queueBind(DEAD_QUEUE_NAME, DEAD_EXCHANGE_NAME, "lisi");
        System.out.println("等待接收消息，吧接收到的消息打印到屏幕上。。。。。。");
        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer01控制台打印接收到的消息："
                    + new String(message.getBody(),
                    StandardCharsets.UTF_8));
        };
        // 消费者取消消息时回调接口
        channel.basicConsume(NORMAL_QUEUE_NAME, true, deliverCallback,
                consumerTag -> {
                });
    }


}
