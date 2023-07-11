package com.image.rabbitmq.producer.exchange;

import com.image.rabbitmq.util.RabbitMqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/7/11 21:22
 */
public class DirectEmitLog {

    private static final String exchange_name = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel(false);
        channel.exchangeDeclare(exchange_name, BuiltinExchangeType.DIRECT);

        //创建多个绑定
        Map<String, String> routingMap = new HashMap<>();
        routingMap.put("info", "info 消息...");
        routingMap.put("waring", "warning 消息...");
        routingMap.put("error", "error 消息...");
        routingMap.forEach((rk, rv) -> {

            //routingKey[info/waring/error] -> Map -> 特定环境
            //可以根据key特殊处理对应的业务
            try {
                channel.basicPublish(exchange_name, rk, null, rv.getBytes());
            } catch (IOException e) {
                System.out.println("发送消息异常：" + e.getMessage());
            }
            System.out.println(DirectEmitLog.class.getSimpleName() + "发送消息：" + rv);
        });


    }
}
