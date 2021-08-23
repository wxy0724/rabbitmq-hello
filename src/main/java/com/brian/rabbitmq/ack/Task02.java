package com.brian.rabbitmq.ack;

import com.brian.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * @author brian
 * 消息在手动应答时是不丢失、放回队列中重新消费
 */
public class Task02 {

    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.basicQos(1);

        boolean queueDurable = true;
        channel.queueDeclare(QUEUE_NAME, queueDurable, false, false, null);
        // 从控制台当中接收信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes("UTF-8"));
            System.out.println("发送消息完成：" + message);
        }
    }

}
