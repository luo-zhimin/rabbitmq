package com.image.rabbitmq.consumer;

import com.image.rabbitmq.config.ConfirmConfig;
import com.image.rabbitmq.util.RabbitMqUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/7/6 21:45
 * 消费者
 */
@Component
@Slf4j
public class Consumer {

    private static final String queue_name = "hello";

    //接收消息
    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtil.getChannel(false);

        //声明 消费者未成功消费的回调
        //(String consumerTag, Delivery message)
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };

        //取消消费的回调
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费消息被中断");
        };

        //消费者 消费消息
        //String queue 消费哪个队列
        //boolean autoAck 消费成功之后是否要自动应答 true
        // DeliverCallback deliverCallback 消费者未成功消费的回调
        // Consumer callback 消费者取消消费的回调
        channel.basicConsume(queue_name, true, deliverCallback, cancelCallback);
        System.out.println("消费成功～");
    }

    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE)
    public void receiveConfirmMessage(Message message) {
        String msg = new String(message.getBody());
        log.info("接收到的队列confirm.queue消息：{}", msg);
    }
}
