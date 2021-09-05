package com.brian.rabbitmq.dircet;

import com.brian.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * @author brian
 * @date 2021/8/23
 */
public class ReceiveLogs02 {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 声明一个队列 临时队列，队列的名称是随机的
        // 绑定交换机与队列
        String queueName = "disk";
        channel.queueDeclare(queueName, false, false, false, null);
        String routingKey = "error";
        channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
        System.out.println("等待接收消息，吧接收到的消息打印到屏幕上。。。。。。");
        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs01控制台打印接收到的消息："
                    + new String(message.getBody(),
                    StandardCharsets.UTF_8));
        };
        // 消费者取消消息时回调接口

        channel.basicConsume(queueName, true, deliverCallback,
                consumerTag -> {});
    }

}
