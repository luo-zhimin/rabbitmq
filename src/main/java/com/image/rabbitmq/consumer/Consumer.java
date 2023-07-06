package com.image.rabbitmq.consumer;

import com.rabbitmq.client.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/7/6 21:45
 * 消费者
 */
public class Consumer {

    private static final String queue_name = "hello";

    //接收消息
    public static void main(String[] args) throws Exception {
        //创建工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("1.15.98.60");
        factory.setUsername("admin");
        factory.setPassword("123");
        //创建信道
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明 消费者未成功消费的回调
        //(String consumerTag, Delivery message)
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };

        //取消消费的回调
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费消息被中断");
        };

        //消费者 消费消息
        //String queue 消费哪个队列
        //boolean autoAck 消费成功之后是否要自动应答 true
        // DeliverCallback deliverCallback 消费者未成功消费的回调
        // Consumer callback 消费者取消消费的回调
        channel.basicConsume(queue_name, true, deliverCallback, cancelCallback);
        System.out.println("消费成功～");
    }
}
