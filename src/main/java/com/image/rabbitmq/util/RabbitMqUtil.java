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

    @SneakyThrows
    public static Channel getChannel() {
        //创建工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("1.15.98.60");
        factory.setUsername("admin");
        factory.setPassword("123");
        //创建链接
        Connection connection = factory.newConnection();
        return connection.createChannel();
    }
}
