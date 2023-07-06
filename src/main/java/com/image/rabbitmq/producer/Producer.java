package com.image.rabbitmq.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2023/7/4 23:31
 * 生产者：发送消息
 */
public class Producer {

    //队列名称
    private final static String queue_name = "hello";

    //消息发送
    public static void main(String[] args) throws Exception{
        //创建工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置工厂的ip 链接队列
        factory.setHost("1.15.98.60");
        factory.setUsername("admin");
        factory.setPassword("123");
        //创建链接
        Connection connection = factory.newConnection();
        //channel
        //获取信道
        Channel channel = connection.createChannel();
        //队列
        //String queue 队列名称,
        //boolean durable 队列里面的消息是否持久化（磁盘），默认情况消息存储在内存中
        //boolean exclusive 该队列是否只供一个消费者消费 是否进行消息的共享 true 多个消费者消费 false 单个
        //boolean autoDelete 是否自动删除 最后一个消费者断开连接后 该队列是否自动删除 true delete false no delete
        //Map<String, Object> arguments 其他参数
        channel.queueDeclare(queue_name,true,false,false,null);
        //发消息 初次使用
        String message ="hello word";
        //basicPublish
        //String exchange 发送到哪个交换机
        //String routingKey 路由key值 本次是队列名称
        //BasicProperties props 其他参数
        //byte[] body 发送的消息
        channel.basicPublish("",queue_name,null,message.getBytes());
        System.out.println("消息发送完毕");
    }
}
