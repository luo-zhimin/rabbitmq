package com.image.rabbitmq.consumer.exchange.direct;

import com.image.rabbitmq.util.RabbitMqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/7/10 22:51
 */
public class DirectReceiveLogSecond {

    private static final String exchange_name = "direct_logs";

    private final static String file_path = "/Users/luozhimin/Desktop/File/daily/";

    private static final String queue = "disk";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel(false);
        channel.exchangeDeclare(exchange_name, BuiltinExchangeType.DIRECT);
        //info waring error(write) 多重绑定
        channel.queueDeclare(queue, false, false, false, null);
        channel.queueBind(queue, exchange_name, "error");

        DeliverCallback deliverCallback = (tag, deliveryMessage) -> {
            String message = new String(deliveryMessage.getBody());
            File file = new File(file_path + "direct_error.txt");
            FileUtils.write(file, message, StandardCharsets.UTF_8, true);
            System.out.println(DirectReceiveLogSecond.class.getSimpleName() + "接收到消息：" + message);
        };

        channel.basicConsume(queue, false, deliverCallback, (message) -> {
            System.out.println(DirectReceiveLogSecond.class.getCanonicalName() + "链接中断");
        });
    }
}
