package com.brian.rabbitmq.deadletter;

import com.brian.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.brian.rabbitmq.deadletter.Consumer01.DEAD_EXCHANGE_NAME;

/**
 * @author brian
 * @date 2021/8/29
 */
public class Producer {

    public static final String NORMAL_EXCHANGE_NAME = "normal_exchange";

    public static final String NORMAL_QUEUE_NAME = "normal_queue";
    public static final String DEAD_QUEUE_NAME = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        // 过期时间
        //AMQP.BasicProperties properties = new AMQP.BasicProperties()
        //        .builder().expiration("10000").build();
        for (int i = 1; i < 11; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE_NAME, "zhangsan",
                     null, message.getBytes());
        }

    }


}
