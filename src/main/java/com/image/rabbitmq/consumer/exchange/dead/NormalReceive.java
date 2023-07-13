package com.image.rabbitmq.consumer.exchange.dead;

import com.image.rabbitmq.util.RabbitMqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * 死信队列->正常队列 进行转发
 *
 * @Author : 镜像
 * @create 2023/7/12 21:19
 */
public class NormalReceive {

    private static final String exchange = "normal_exchange";

    private static final String dead_exchange = "dead_exchange";

    private static final String queue = "normal_queue";

    private static final String dead_queue = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel(false);
        //声明交换机
        channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(dead_exchange, BuiltinExchangeType.DIRECT);
        //声明队列 (1.消息被拒绝 2.消息ttl过期 3.队列达到最大长度)
        Map<String, Object> arguments = new HashMap<>();
        //正常队列需要设置死信交换机
        arguments.put("x-dead-letter-exchange", dead_exchange);
        //设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "dead");
        //过期时间 毫秒 生产者指定 灵活
//        arguments.put("x-message-ttl",10000);
        //指定队列最大长度
//        arguments.put("x-max-length",6);

        channel.queueDeclare(queue, false, false, false, arguments);
        //死信队列
        channel.queueDeclare(dead_queue, false, false, true, null);

        //绑定
        channel.queueBind(queue, exchange, "normal");
        channel.queueBind(dead_queue, dead_exchange, "dead");

        DeliverCallback deliverCallback = (tag, deliveryMessage) -> {
            String message = new String(deliveryMessage.getBody());
            //reject
            if (message.equals("info4")) {
                System.out.println(NormalReceive.class.getSimpleName() + "拒收消息：" + message);
                //标识 是否放回队列(该队列[普通队列])
                channel.basicReject(deliveryMessage.getEnvelope().getDeliveryTag(), false);
            }
            System.out.println(NormalReceive.class.getSimpleName() + "接收到消息：" + message);
            channel.basicAck(deliveryMessage.getEnvelope().getDeliveryTag(), false);
        };

        //手动应答
        channel.basicConsume(queue, false, deliverCallback, (message) -> {
            System.out.println(NormalReceive.class.getSimpleName() + "中断接收....");
        });
    }
}
