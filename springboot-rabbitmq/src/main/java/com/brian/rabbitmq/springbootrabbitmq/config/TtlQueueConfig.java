package com.brian.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author brian
 * @date 2021/8/31
 */
@Configuration
public class TtlQueueConfig {

    /**
     * 普通交换机
     */
    public static final String X_EXCHANGE = "x";

    /**
     * 死信交换机
     */
    public static final String Y_DEAD_LETTER_EXCHANGE = "y";

    /**
     * 普通队列
     */
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";

    /**
     * 死信队列
     */
    public static final String DEAD_LETTER_QUEUE = "QD";

    public static final String QUEUE_C = "QC";


    /**
     * 声明X_EXCHANGE
     */
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    /**
     * 声明Y_DEAD_LETTER_EXCHANGE
     */
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    /**
     * 声明队列
     */
    @Bean("queueA")
    public Queue queueA() {
        return QueueBuilder.durable(QUEUE_A)
                .ttl(10000)
                .deadLetterRoutingKey("YD")
                .deadLetterExchange(Y_DEAD_LETTER_EXCHANGE)
                .build();
    }

    @Bean("queueB")
    public Queue queueB() {
        return QueueBuilder.durable(QUEUE_B)
                .ttl(40000)
                .deadLetterRoutingKey("YD")
                .deadLetterExchange(Y_DEAD_LETTER_EXCHANGE)
                .build();
    }

    @Bean("queueD")
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    /**
     * 绑定
     */
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }

    @Bean("queueC")
    public Queue queueC() {
        return QueueBuilder.durable(QUEUE_C)
                .deadLetterRoutingKey("YD")
                .deadLetterExchange(Y_DEAD_LETTER_EXCHANGE)
                .build();
    }

    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }

}
