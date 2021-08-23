package com.brian.rabbitmq.ack;

import com.brian.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.concurrent.TimeUnit;

/**
 * @author brian
 * 这是一个工作线程
 */
public class WorkerAck02 {

    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        // 声明
        DeliverCallback deliverCallback = (consumerTag, deliver)-> {
            System.out.println("C2等待接收消息！");
            String message = new String(deliver.getBody(), "UTF-8");
            try {
                TimeUnit.SECONDS.sleep(30L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("接收到消息" + message);
            // 手动应答
            // arg1 消息tag
            // aeg2 false 代表只应答接收到的那个传递的信息，true 为应答所有消息包括传递过来的消息
            channel.basicAck(deliver.getEnvelope().getDeliveryTag(), false);
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消息者取消消费借口回调逻辑");
        };
        boolean autoAck = false;
        // 消息的接收
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);

    }

}
