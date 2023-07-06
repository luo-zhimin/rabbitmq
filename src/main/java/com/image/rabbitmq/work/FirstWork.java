package com.image.rabbitmq.work;

import com.image.rabbitmq.util.RabbitMqUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/7/6 22:29
 * 工作线程 消费者
 */
public class FirstWork {

    private static final String queue_name = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收到的消息：" + new String(message.getBody()));
        };

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("continue");
        };

        //多次运行 -- edit config allow
        System.out.println("work2等待接收消息......");
        //消息的接收
        channel.basicConsume(queue_name, true, deliverCallback, cancelCallback);
    }
}
