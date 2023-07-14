package com.image.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * ttl队列 消费者
 *
 * @Author : 镜像
 * @create 2023/7/14 21:17
 */
@Component
@Slf4j
public class DeadLetterQueueConsumer {

    //接受消息
    @RabbitListener(queues = "QD")
    @SneakyThrows
    public void receive(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("当前时间:{},收到死信队列消息->{}", new Date(), msg);
    }
}
