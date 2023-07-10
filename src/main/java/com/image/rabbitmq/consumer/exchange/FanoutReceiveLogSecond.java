package com.image.rabbitmq.consumer.exchange;

import com.image.rabbitmq.util.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * Created by IntelliJ IDEA.
 * fanout 发布订阅模式 接收(1)
 *
 * @Author : 镜像
 * @create 2023/7/10 21:59
 */
public class FanoutReceiveLogSecond {

    private final static String exchange_name = "logs";

    private final static String file_path = "/Users/luozhimin/Desktop/File/daily/";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel(false);
        //声明交换机
        channel.exchangeDeclare(exchange_name, "fanout");
        //声明队列 - 临时队列
        //队列的名称 随机 当消费者断开链接之后 自动删除
        String queue = channel.queueDeclare().getQueue();
        //绑定交换机和队列
        channel.queueBind(queue, exchange_name, "");
        System.out.println(FanoutReceiveLogSecond.class.getName() + "等待接收消息，把接收到的消息打印到屏幕.....");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String receiveMessage = new String(message.getBody());
            //存储到磁盘
            File file = new File(file_path + "rabbit_mq.txt");
            FileUtils.write(file, receiveMessage, StandardCharsets.UTF_8, true);
            System.out.println(FanoutReceiveLogSecond.class.getName() + "接收到：" + receiveMessage);
        };

        channel.basicConsume(queue, true, deliverCallback, (message) -> {
            System.out.println(FanoutReceiveLogSecond.class.getName() + "接收中断....");
        });
    }
}
