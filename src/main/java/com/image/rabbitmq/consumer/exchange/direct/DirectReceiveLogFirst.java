package com.image.rabbitmq.consumer.exchange.direct;

import com.image.rabbitmq.util.RabbitMqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/7/10 22:51
 */
public class DirectReceiveLogFirst {

    private static final String exchange_name = "direct_logs";

    private static final String queue = "console";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel(false);
        channel.exchangeDeclare(exchange_name, BuiltinExchangeType.DIRECT);
        //info waring error(write) 多重绑定
        channel.queueDeclare(queue, false, false, false, null);
        channel.queueBind(queue, exchange_name, "info");
        channel.queueBind(queue, exchange_name, "waring");

        DeliverCallback deliverCallback = (tag, deliveryMessage) -> {
            String message = new String(deliveryMessage.getBody());
            System.out.println(DirectReceiveLogFirst.class.getSimpleName() + "接收到消息：" + message);
        };

        channel.basicConsume(queue, false, deliverCallback, (message) -> {
            System.out.println(DirectReceiveLogFirst.class.getCanonicalName() + "链接中断");
        });
    }
}
