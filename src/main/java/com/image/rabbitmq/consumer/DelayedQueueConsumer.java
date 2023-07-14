package com.image.rabbitmq.consumer;

import com.image.rabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * 消费者 - 基于插件的延迟消息
 *
 * @Author : 镜像
 * @create 2023/7/14 23:14
 */
@Component
@Slf4j
public class DelayedQueueConsumer {

    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE)
    public void delayReceive(Message message) {
        /*
            延时队列在需要延时处理的场景下非常有用，使用 RabbitMQ 来实现延时队列可以很好的利用
            RabbitMQ 的特性，如：消息可靠发送、消息可靠投递、死信队列来保障消息至少被消费一次以及未被正 确处理的消息不会被丢弃。
            另外，通过 RabbitMQ 集群的特性，可以很好的解决单点故障问题，不会因为 单个节点挂掉导致延时队列不可用或者消息丢失

            死信队列 - x-dead-letter-exchange 不好 排队
            延迟队列 - x-delayed-message 按照时间处理
         */
        String msg = new String(message.getBody());
        log.info("当前时间:{},收到延迟队列(delayed.queue)的消息->{}", new Date(), msg);
    }
}
