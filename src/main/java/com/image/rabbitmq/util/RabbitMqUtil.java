package com.image.rabbitmq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/7/6 22:08
 */
public class RabbitMqUtil {

    /**
     * 获取mq的信道
     *
     * @param confirm 是否开启发布确认
     * @return 信道
     */
    @SneakyThrows
    public static Channel getChannel(Boolean confirm) {
        //创建工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("1.15.98.60");
        factory.setUsername("admin");
        factory.setPassword("123");
        //创建链接
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        if (confirm) {
            //开始发布确认模式
            channel.confirmSelect();
        }
        return channel;
    }
}
