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

    //备份交换机
    public static final String BACKUP_EXCHANGE = "back_exchange";

    //备份队列
    public static final String BACKUP_QUEUE = "backup_queue";

    //报警队列
    public static final String WARING_QUEUE = "waring_queue";

    @Bean
    public DirectExchange confirmExchange() {
//        return new DirectExchange(CONFIRM_EXCHANGE);
        return ExchangeBuilder
                .directExchange(CONFIRM_EXCHANGE)
                .durable(true)
                //明确备份交换机
                .withArgument("alternate-exchange", BACKUP_EXCHANGE)
                .build();
    }

    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE);
    }

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder
                .durable(CONFIRM_QUEUE)
                .build();
    }

    @Bean
    public Queue backupQueue() {
        return QueueBuilder
                .durable(BACKUP_QUEUE)
                .build();
    }

    @Bean
    public Queue waringQueue() {
        return QueueBuilder
                .durable(WARING_QUEUE)
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

    @Bean
    public Binding bindingBackupQueue(@Qualifier("backupExchange") FanoutExchange exchange,
                                      @Qualifier("backupQueue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange);
    }

    @Bean
    public Binding bindingWaringQueue(@Qualifier("backupExchange") FanoutExchange exchange,
                                      @Qualifier("waringQueue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange);
    }
}
