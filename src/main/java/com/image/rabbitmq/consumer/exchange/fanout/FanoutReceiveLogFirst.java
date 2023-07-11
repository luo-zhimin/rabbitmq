package com.image.rabbitmq.consumer.exchange.fanout;

import com.image.rabbitmq.util.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * Created by IntelliJ IDEA.
 * fanout 发布订阅模式 接收(1)
 *
 * @Author : 镜像
 * @create 2023/7/10 21:59
 */
public class FanoutReceiveLogFirst {

    private final static String exchange_name = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel(false);
        //声明交换机
        channel.exchangeDeclare(exchange_name, "fanout");
        //声明队列 - 临时队列
        //队列的名称 随机 当消费者断开链接之后 自动删除
        String queue = channel.queueDeclare().getQueue();
        //绑定交换机和队列
        channel.queueBind(queue, exchange_name, "");
        System.out.println(FanoutReceiveLogFirst.class.getName() + "等待接收消息，把接收到的消息打印到屏幕.....");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(FanoutReceiveLogFirst.class.getName() + "接收到：" + new String(message.getBody()));
        };

        channel.basicConsume(queue, true, deliverCallback, (message) -> {
            System.out.println(FanoutReceiveLogFirst.class.getName() + "接收中断....");
        });
    }
}
