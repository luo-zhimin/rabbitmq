package com.image.rabbitmq.producer;

import com.image.rabbitmq.util.RabbitMqUtil;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * 手动应答时不丢失 重新返回队列中 重新消费
 *
 * @Author : 镜像
 * @create 2023/7/9 18:15
 */
public class HandTaskProducer {

    private static final String task_queue = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        //声明队列
        channel.queueDeclare(task_queue, false, false, false, null);
        //控制台输入
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String next = scanner.next();
            System.out.println("手动处理（生产者）发送的消息是：" + next);
            channel.basicPublish("", task_queue, null, next.getBytes(StandardCharsets.UTF_8));
        }

    }
}
