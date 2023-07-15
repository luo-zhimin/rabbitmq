package com.image.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * 发布确认
 *
 * @Author : 镜像
 * @create 2023/7/15 21:21
 */
@Configuration
public class ConfirmConfig {

    //交换机
    public static final String CONFIRM_EXCHANGE = "confirm.exchange";
    //队列
    public static final String CONFIRM_QUEUE = "confirm.queue";
    //routingKey
    public static final String CONFIRM_ROUTING_KEY = "confirm.routingKey";

    @Bean
    public DirectExchange confirmExchange() {
        return new DirectExchange(CONFIRM_EXCHANGE);
    }

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder
                .durable(CONFIRM_QUEUE)
                .build();
    }

    @Bean
    public Binding binding(@Qualifier("confirmExchange") DirectExchange exchange,
                           @Qualifier("confirmQueue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(CONFIRM_ROUTING_KEY);
    }

}
