package com.brian.rabbitmq.origin;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 * @author brian
 */
public class Producer {

    /**
     * 队列名称
      */
    public static final String QUEUE_NAME = "hello";

    /**
     * 发消息
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
        // 生成一个队列
        // arg1 队列名称
        // arg2 队列里面的消息是否持久化，默认消息存储在内存中
        // arg3 该队列是否只供一个消费者进行消费 是否进行消息共享 true可以多个消费者消费
        // arg4 是否自动删除 最后一个消费者断开连接以后，该队列是否自动删除
        // arg5 其他参数
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 发消息
        String message = "hello world!";
        // 发送一个消息
        // arg1 发送到哪个交换机
        // arg2 路由的KEY健值是哪个 本次是队列的名称
        // arg3 其他参数信息
        // arg4 发送消息的消息题
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

    }

}
