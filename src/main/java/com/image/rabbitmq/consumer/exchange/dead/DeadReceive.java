package com.image.rabbitmq.consumer.exchange.dead;

import com.image.rabbitmq.util.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * Created by IntelliJ IDEA.
 * 死信队列->正常队列 进行转发
 *
 * @Author : 镜像
 * @create 2023/7/12 21:19
 */
public class DeadReceive {

    private static final String dead_queue = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel(false);

        System.out.println("等待接收消息...");
        DeliverCallback deliverCallback = (tag, deliveryMessage) -> {
            String message = new String(deliveryMessage.getBody());
            System.out.println(DeadReceive.class.getSimpleName() + "接收到消息：" + message);
        };

        channel.basicConsume(dead_queue, true, deliverCallback, (message) -> {
            System.out.println(DeadReceive.class.getSimpleName() + "中断接收....");
        });
    }
}
