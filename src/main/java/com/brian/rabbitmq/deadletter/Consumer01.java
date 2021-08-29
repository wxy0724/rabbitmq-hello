package com.brian.rabbitmq.dead;

import com.brian.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

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

        channel.queueDeclare(NORMAL_QUEUE_NAME, false, false,
                false, null);
        channel.queueDeclare(DEAD_QUEUE_NAME, false, false,
                false, null);

        channel.queueBind(queueName, EXCHANGE_NAME, routingKey2);
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
