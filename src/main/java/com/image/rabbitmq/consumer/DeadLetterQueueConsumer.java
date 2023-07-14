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
        /*
            因为 RabbitMQ 只会检查第一个消息是否过期，如果过期则丢到死信队列，
            如果第一个消息的延时时长很长，而第二个消息的延时时长很短，第二个消息并不会优先得到执行
         */
        String msg = new String(message.getBody());
        log.info("当前时间:{},收到死信队列消息->{}", new Date(), msg);
    }
}
