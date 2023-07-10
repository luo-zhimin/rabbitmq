package com.image.rabbitmq.confirm;

import com.image.rabbitmq.util.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/7/9 22:11
 * 发布确认模式 -- 使用的时间 比较哪种方式最好
 * 单个模式 --- 29742ms
 * 批量确认 --- 301ms
 * 异步批量确认 --- 22ms
 */
public class ConfirmMessage {

    private static final int messageCount = 1000;

    /**
     * 同步等待确认，简单，但吞吐量非常有限
     * 批量发布消息
     * 批量同步等待确认，简单，合理的吞吐量， 一旦出现问题但很难推断出是那条消息出现了问题
     * 异步处理：
     * 最佳性能和资源使用，在出现错误的情况下可以很好地控制，但是实现起来稍微难些
     */

    public static void main(String[] args) throws Exception {
        //单个确认 -- 29742 ms
//        publishMessageSingle();
        //批量确认 -- 301ms
//        publishBatchMessage();
        //异步批量确认  22ms  -> 27ms
        publishAsyncBatchMessage();
    }


    /**
     * 单个确认
     */
    public static void publishMessageSingle() throws Exception {
        Channel channel = RabbitMqUtil.getChannel(true);
        String queueName = UUID.randomUUID().toString();
        //声明队列
        channel.queueDeclare(queueName, false, false, false, null);
        //开始时间
        long start = System.currentTimeMillis();

        //批量发送消息
        for (int i = 0; i < messageCount; i++) {
            String message = String.valueOf(i);
            channel.basicPublish("", queueName, null, message.getBytes());
            //马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功");
            }
        }

        System.out.println("发布" + messageCount + "条，" + "单个确认用时: " + (System.currentTimeMillis() - start) + "ms");
    }

    /**
     * 批量发布
     */
    public static void publishBatchMessage() throws Exception {
        Channel channel = RabbitMqUtil.getChannel(true);
        String queueName = UUID.randomUUID().toString();
        //声明队列
        channel.queueDeclare(queueName, false, false, false, null);
        //开始时间
        long start = System.currentTimeMillis();

        //批量确认消息的大小
        int batchSize = 100;

        //批量发送消息 -- 批量发布确认
        for (int i = 1; i <= messageCount; i++) {
            String message = String.valueOf(i);
            channel.basicPublish("", queueName, null, message.getBytes());

            if (batchSize % i == 0) {
                //马上进行发布确认
                boolean flag = channel.waitForConfirms();
                if (flag) {
                    System.out.println("消息发送成功");
                }
            }
        }

        System.out.println("发布" + messageCount + "条，" + "批量确认用时: " + (System.currentTimeMillis() - start) + "ms");
    }

    /**
     * 异步批量发布
     */
    public static void publishAsyncBatchMessage() throws Exception {
        Channel channel = RabbitMqUtil.getChannel(true);
        String queueName = UUID.randomUUID().toString();
        //声明队列
        channel.queueDeclare(queueName, false, false, false, null);

        //线程安全有序的hash表 适用于高并发
        //1.轻松的将序号和消息进行关联 2.轻松的批量删除(key) 3.支持高并发(多线程)
        ConcurrentSkipListMap<Long, String> messageMap = new ConcurrentSkipListMap<>();

        //开始时间
        long start = System.currentTimeMillis();

        //消息的监听器 监听消息的成功/失败
        //ConfirmCallback ackCallback 成功
        //ConfirmCallback nackCallback 未成功

        //deliveryTag 消息确认的编号
        // multiple 是否是批量
        //消息确认
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            //2.删除到已经确认的消息
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confirmed = messageMap.headMap(deliveryTag);
                confirmed.clear();
                return;
            }
            messageMap.remove(deliveryTag);
            System.out.println("确认消息的编号: " + deliveryTag);
        };

        //消息失败
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            //3.打印一下未确认的消息
            String message = messageMap.get(deliveryTag);
            System.out.println("未确认消息的编号: " + deliveryTag + ",消息是: " + message);
        };

        //异步监听
        channel.addConfirmListener(ackCallback, nackCallback);

        //异步
        for (int i = 0; i < messageCount; i++) {
            String message = "消息" + i;
            channel.basicPublish("", queueName, null, message.getBytes());
            //1.记录下所有要发送的消息 消息的总和 channel.getNextPublishSeqNo() 下一步准备发送的序号
            messageMap.put(channel.getNextPublishSeqNo(), message);
        }

        System.out.println("发布" + messageCount + "条，" + "异步批量确认用时: " + (System.currentTimeMillis() - start) + "ms");
    }
}
