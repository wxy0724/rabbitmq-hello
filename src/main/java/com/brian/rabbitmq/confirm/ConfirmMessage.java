package com.brian.rabbitmq.confirm;

import com.brian.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author brian
 * @date 2021/8/21
 * 发布确认模式，使用的时间，比较那种确认方式是最好的
 * 1.单个确认
 * 2.批量确认
 * 3.异步批量确认
 */
public class ConfirmMessage {

    /**
     * 批量发消息的个数
     *
     * @param args
     */
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        //1.单个确认，发布1000个单独确认消息，耗时：41461ms
        //publishMessageIndividually();
        //2.批量确认，发布1000个单独确认消息，耗时：523ms
        //publishMessageBatch();
        //3.异步批量确认
        publishMessageAsync();
    }

    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false,
                null);
        // 开启发布确认模式
        channel.confirmSelect();
        /*
         *  线程安全有序的一个哈希表，适用于高并发的情况下
         * 1.轻松的讲序号与消息进行关联
         * 2.轻松批量删除条目，只要给到序号
         * 3.支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();
        long beginTime = System.currentTimeMillis();
        // 准备消息的监听器 监听那些消息成功了 那些消息失败了
        // 消息确认成功，回调函数
        ConfirmCallback ackConfirmCallback = (deliveryTag, multiple) -> {
            // 2.删除掉已经确认的消息
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confirmed =
                        outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            } else {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认的消息：" + deliveryTag);
        };
        /*
         * 消息确认失败，回调函数
         * 1.消息的标记
         * 2.是否为批量确认
         */
        ConfirmCallback nackConfirmCallback = (deliveryTag, multiple) -> {
            // 3.打印未确认的消息
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息是：" +message + "，未确认的消息tag："+ deliveryTag);
        };
        channel.addConfirmListener(ackConfirmCallback, nackConfirmCallback);
        //批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null,
                    message.getBytes());
            // 1.记录所有要发送的消息
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步确认消息，耗时：" + (endTime - beginTime) + "ms");

    }

    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认模式
        channel.confirmSelect();
        long beginTime = System.currentTimeMillis();
        //批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
            // 单个消息就马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功！");
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时：" + (endTime - beginTime) + "ms");

    }

    public static void publishMessageBatch() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认模式
        channel.confirmSelect();
        long beginTime = System.currentTimeMillis();
        // 批量确认消息大小
        int batchSize = 100;
        //批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null,
                    message.getBytes());
            if (i % batchSize == 0) {
                boolean flag = channel.waitForConfirms();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时：" + (endTime - beginTime) + "ms");

    }

}
