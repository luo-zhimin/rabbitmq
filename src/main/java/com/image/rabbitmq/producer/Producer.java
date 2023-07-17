package com.image.rabbitmq.producer;

import com.image.rabbitmq.util.RabbitMqUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/7/4 23:31
 * 生产者：发送消息
 */
public class Producer {

    //队列名称
    private final static String queue_name = "hello";

    //消息发送
    public static void main(String[] args) throws Exception {

        //channel
        //获取信道
        Channel channel = RabbitMqUtil.getChannel(false);
        //队列
        //String queue 队列名称,
        //boolean durable 队列里面的消息是否持久化（磁盘），默认情况消息存储在内存中
        //boolean exclusive 该队列是否只供一个消费者消费 是否进行消息的共享 true 多个消费者消费 false 单个
        //boolean autoDelete 是否自动删除 最后一个消费者断开连接后 该队列是否自动删除 true delete false no delete
        //Map<String, Object> arguments 其他参数

        Map<String, Object> arguments = new HashMap<>(1);
        //最大优先级 官方容许最大0-255 不要设置太大 浪费cpu和内存
        arguments.put("x-max-priority", 10);
        //惰性队列 --> 直接写进磁盘里面
//        arguments.put("x-queue-mode", "lazy");

        channel.queueDeclare(queue_name, true, false, false, arguments);
        //发消息 初次使用
//        String message = "hello word";
        //basicPublish
        //String exchange 发送到哪个交换机
        //String routingKey 路由key值 本次是队列名称
        //BasicProperties props 其他参数
        //byte[] body 发送的消息
        for (int i = 0; i < 10; i++) {
            String message = "info" + i;
            if (i == 5) {
                //优先级高一些
                AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
                        .priority(5)
                        .build();
//                basicProperties.builder().priority(5).build();
                channel.basicPublish("", queue_name, basicProperties, message.getBytes());
                ;
            } else {
                channel.basicPublish("", queue_name, null, message.getBytes());
            }
        }
        System.out.println("消息发送完毕");
    }
}
