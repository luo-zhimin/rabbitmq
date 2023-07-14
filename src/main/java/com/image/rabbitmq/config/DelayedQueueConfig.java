package com.image.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * 延迟队列 配置
 *
 * @Author : 镜像
 * @create 2023/7/14 22:39
 */
@Configuration
public class DelayedQueueConfig {

    //延时交换机
    public static final String DELAYED_EXCHANGE = "delayed.exchange";

    //延时队列
    public static final String DELAYED_QUEUE = "delayed.queue";

    //延时routingKey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    @Bean
    //声明交换机 基于plugins
    public CustomExchange delayedExchange() {
        //String name 交换机的名字
        // String type 交换机的类型
        // boolean durable 是否持久化
        // boolean autoDelete 是否自动删除
        // Map<String, Object> arguments 拓展参数
        //todo  !!! x-delayed-message plugins
        return new CustomExchange(DELAYED_EXCHANGE, "x-delayed-message", true, false, new HashMap<String, Object>(3) {{
            put("x-delayed-type", "direct");
//            put("x-delayed-type","direct");
        }});
    }

    @Bean
    public Queue delayedQueue() {
        return QueueBuilder.durable(DELAYED_QUEUE).build();
    }

    @Bean
    public Binding delayedBing(@Qualifier("delayedQueue") Queue queue,
                               @Qualifier("delayedExchange") CustomExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(DELAYED_ROUTING_KEY)
                .noargs();
    }
}
