package com.brian.rabbitmq.worker;

import com.brian.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * @author brian
 * 这是一个工作线程
 */
public class Worker02 {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException {
        Channel channel = null;
        try {
            channel = RabbitMqUtils.getChannel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 声明
        DeliverCallback deliverCallback = (consumerTag, message)-> {
            System.out.println("接收到的消息：" + new String(message.getBody()));
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消息者取消消费借口回调逻辑");
        };
        System.out.println("C2等待接收消息！");
        // 消息的接收
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);

    }

}
