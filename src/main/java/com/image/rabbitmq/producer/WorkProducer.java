package com.image.rabbitmq.producer;

import com.image.rabbitmq.util.RabbitMqUtil;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * 工作队列 - 生产者
 *
 * @Author : 镜像
 * @create 2023/7/6 23:05
 */
public class WorkProducer {

    private static final String queue_name = "hello";

    //准备发送大量的消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel(false);
        //队列的声明
        channel.queueDeclare(queue_name, true, false, false, null);
        //发送消息 控制台输入
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String next = scanner.next();
            channel.basicPublish("", queue_name, null, next.getBytes());
            System.out.println("发送消息完成: " + next);
        }
    }
}
