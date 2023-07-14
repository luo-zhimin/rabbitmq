package com.image.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * ttl 队列
 *
 * @Author : 镜像
 * @create 2023/7/13 22:29
 */
@Configuration
public class TtlQueueConfig {

    //普通交换机
    private static final String x_exchange = "X";
    //死信交换机
    private static final String dead_letter_exchange = "Y";
    //普通队列
    private static final String queue_a = "QA";
    private static final String queue_b = "QB";
    private static final String queue_c = "QC";

    //死信队列
    private static final String dead_letter_queue = "QD";

    @Bean("xExchange")
    public DirectExchange xExchange() {
        //声明普通交换机
        return new DirectExchange(x_exchange);
    }

    @Bean("deadExchange")
    public DirectExchange deadExchange() {
        //声明死信交换机
        return new DirectExchange(dead_letter_exchange);
    }

    @Bean("queueA")
    public Queue queueA() {
        //可以自己写配置 也可以直接 builder.
        Map<String, Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange", dead_letter_exchange);
        arguments.put("x-dead-letter-routing-key", "YD");
        arguments.put("x-message-ttl", 10000);
        //声明普通队列
        return QueueBuilder.durable(queue_a)
                .withArguments(arguments)
                .build();
    }

    @Bean("queueC")
    public Queue queueC() {
        return QueueBuilder.durable(queue_c)
                .deadLetterExchange(dead_letter_exchange)
                .deadLetterRoutingKey("YD")
                .build();
    }

    @Bean("queueB")
    public Queue queueB() {
        //可以自己写配置 也可以直接 builder.
        Map<String, Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange", dead_letter_exchange);
        arguments.put("x-dead-letter-routing-key", "YD");
        arguments.put("x-message-ttl", 40000);
        //声明普通队列
        return QueueBuilder.durable(queue_b)
                .withArguments(arguments)
                .build();
    }

    @Bean("deadQueue")
    public Queue deadQueue() {
        //声明普通交换机
        return QueueBuilder.durable(dead_letter_queue)
                .build();
    }

    //绑定
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queue,
                                  @Qualifier("xExchange") DirectExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("XA");
    }

    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queue,
                                  @Qualifier("xExchange") DirectExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("XB");
    }

    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queue,
                                  @Qualifier("xExchange") DirectExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("XC");
    }

    @Bean
    public Binding queueDBindingY(@Qualifier("deadQueue") Queue queue,
                                  @Qualifier("deadExchange") DirectExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("YD");
    }
}
