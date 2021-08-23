package com.brian.rabbitmq.origin;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author brian
 * 消费者
 */
public class Consumer {

    /**
     * 队列名称
     */
    public static final String QUEUE_NAME = "hello";

    /**
     * 接收消息
     * @param args
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建一个工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 工厂IP 连接RabbitMQ的队列
        factory.setHost("47.115.57.152");
        // 用户名
        factory.setUsername("admin");
        // 密码
        factory.setPassword("123");
        // 创建连接
        Connection connection = factory.newConnection();
        // 获取信道
        Channel channel = connection.createChannel();

        // 声明
        DeliverCallback deliverCallback = (consumerTag,message)-> {
            System.out.println(new String(message.getBody()));
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费消息被中断");
        };

        /*
         消费者消费消息
         arg1 队列名称
         arg2 消费成功之后是否要自动应答， true代表的自动应答
         arg3 消费者未成功消费的回调
         arg4 消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }

}
