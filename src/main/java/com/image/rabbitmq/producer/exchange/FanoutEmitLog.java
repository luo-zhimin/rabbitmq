package com.image.rabbitmq.producer.exchange;

import com.image.rabbitmq.util.RabbitMqUtil;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * fanout producer
 * 发布订阅模式
 *
 * @Author : 镜像
 * @create 2023/7/10 22:31
 */
public class FanoutEmitLog {

    private static final String exchange_name = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel(false);
        channel.exchangeDeclare(exchange_name, "fanout");
//        String queue = channel.queueDeclare().getQueue();

        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入消息：");
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(exchange_name, "", null, message.getBytes());
            System.out.println(FanoutEmitLog.class.getName() + "发送消息: " + message);
        }
    }
}
