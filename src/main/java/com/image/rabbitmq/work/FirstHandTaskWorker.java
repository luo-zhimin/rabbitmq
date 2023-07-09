package com.image.rabbitmq.work;

import com.image.rabbitmq.util.RabbitMqUtil;
import com.image.rabbitmq.util.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * Created by IntelliJ IDEA.
 * 手动应答-消费者
 *
 * @Author : 镜像
 * @create 2023/7/9 18:24
 */
public class FirstHandTaskWorker {

    private static final String task_queue = "ack_queue";

    public static void main(String[] args) throws Exception {
        //接收消息
        Channel channel = RabbitMqUtil.getChannel();
        System.out.println("c1等待消息处理时间较短....");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            SleepUtils.sleep(1);
            System.out.println("手动处理（消费者）消费消息：" + new String(message.getBody()));
            //应答
            //long deliveryTag 消息的标记
            // boolean multiple 是否批量应答
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("手动消费中断");
        };

        //手动确认
        boolean autoAck = false;
        //设置不公平分发
//        int prefetchCount = 1;
        //设置预取值(可以堆积多少条) 可以收到多少条 一条确认之后在进来
        int prefetchCount = 2;
        channel.basicQos(prefetchCount);
        channel.basicConsume(task_queue, autoAck, deliverCallback, cancelCallback);
    }
}
