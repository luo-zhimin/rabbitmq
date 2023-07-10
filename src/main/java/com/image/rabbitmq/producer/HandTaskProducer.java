package com.image.rabbitmq.producer;

import com.image.rabbitmq.util.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * 手动应答时不丢失 重新返回队列中 重新消费
 * Producer
 *
 * @Author : 镜像
 * @create 2023/7/9 18:15
 */
public class HandTaskProducer {

    private static final String task_queue = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel(false);

        /*
            inequivalent arg 'durable' for queue 'ack_queue' in vhost '/': received 'true' but current is 'false', class-id=50, method-id=10)
            队列如果之前是未持久化的 突然改成持久化的 会报错 解决方法 需要先删除未持久化的queue 再次生成持久化的队列
         */

        //是否持久化
        //需要队列进行持久化
        boolean durable = true;
        //声明队列
        channel.queueDeclare(task_queue, durable, false, false, null);
        //控制台输入
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String next = scanner.next();
            System.out.println("手动处理（生产者）发送的消息是：" + next);
            //发消息的时候 消息进行持久化 文本
            channel.basicPublish("", task_queue, MessageProperties.PERSISTENT_TEXT_PLAIN, next.getBytes(StandardCharsets.UTF_8));
        }

    }
}
