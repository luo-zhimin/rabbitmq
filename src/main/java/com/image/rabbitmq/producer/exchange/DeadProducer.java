package com.image.rabbitmq.producer.exchange;

import com.image.rabbitmq.util.RabbitMqUtil;
//import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

/**
 * Created by IntelliJ IDEA.
 * 死信队列 生产者
 *
 * @Author : 镜像
 * @create 2023/7/12 22:09
 */
public class DeadProducer {

    private static final String exchange = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel(false);
        //消费者生成之后 生产者可以不生成
        channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT);

        //死信消息
//        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
//                //过期时间 ms
//                .expiration("10000")
//                .build();

        for (int i = 0; i < 10; i++) {
            String message = "info" + i;
            channel.basicPublish(exchange, "normal", null, message.getBytes());
            System.out.println(DeadProducer.class.getSimpleName() + "发送消息: " + message);
        }
    }
}
