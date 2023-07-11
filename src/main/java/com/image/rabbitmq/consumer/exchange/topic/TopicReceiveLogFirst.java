package com.image.rabbitmq.consumer.exchange.topic;

import com.image.rabbitmq.util.RabbitMqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/7/11 22:12
 */
public class TopicReceiveLogFirst {

    private static final String exchange_name = "topic_logs";

    private static final String queue_name = "Q1";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel(false);
        //主题模式
        channel.exchangeDeclare(exchange_name, BuiltinExchangeType.TOPIC);
        //声明队列
        channel.queueDeclare(queue_name, false, false, false, null);
        channel.queueBind(queue_name, exchange_name, "*.orange.*");
        System.out.println(TopicReceiveLogFirst.class.getSimpleName() + "等待接收消息...");
        //接收消息
        channel.basicConsume(queue_name, false, (tag, deliverMessage) -> {
            String message = new String(deliverMessage.getBody());
//            deliverMessage.getEnvelope().getRoutingKey();
            System.out.println(TopicReceiveLogFirst.class.getSimpleName() + " - " + deliverMessage.getEnvelope().getRoutingKey() + "- 接收消息: " + message);
//            channel.basic
        }, (message) -> {
            System.out.println(TopicReceiveLogFirst.class.getSimpleName() + "接收中断消息：" + message);
        });
    }
}
