package com.image.rabbitmq.producer.exchange;

import com.image.rabbitmq.util.RabbitMqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * 主题模式
 *
 * @Author : 镜像
 * @create 2023/7/11 22:10
 */
public class TopicEmitLog {

    /*
        当一个队列绑定键是#,那么这个队列将接收所有数据，就有点像 fanout 了
        如果队列绑定键当中没有#和*出现，那么该队列绑定类型就是 direct 了
     */

    private static final String exchange_name = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel(false);
        channel.exchangeDeclare(exchange_name, BuiltinExchangeType.TOPIC);

        /*
            quick.orange.rabbit 被队列 Q1Q2 接收到
            lazy.orange.elephant 被队列 Q1Q2 接收到
            quick.orange.fox	被队列 Q1 接收到
            lazy.brown.fox	被队列 Q2 接收到
            lazy.pink.rabbit	虽然满足两个绑定但只被队列 Q2 接收一次
            quick.brown.fox	不匹配任何绑定不会被任何队列接收到会被丢弃
            quick.orange.male.rabbit	是四个单词不匹配任何绑定会被丢弃
            lazy.orange.male.rabbit     是四个单词但匹配 Q2
         */
        Map<String, String> bindMap = new HashMap<>();
        bindMap.put("quick.orange.rabbit", "被队列 Q1Q2 接收到");
        bindMap.put("lazy.orange.elephant", "被队列 Q1Q2 接收到");
        bindMap.put("quick.orange.fox", "被队列 Q1 接收到");
        bindMap.put("lazy.brown.fox", "被队列 Q2 接收到");
        bindMap.put("lazy.pink.rabbit", "虽然满足两个绑定但只被队列 Q2 接收一次");
        bindMap.put("quick.brown.fox", "不匹配任何绑定不会被任何队列接收到会被丢弃");
        bindMap.put("quick.orange.male.rabbit", "是四个单词不匹配任何绑定会被丢弃");
        bindMap.put("lazy.orange.male.rabbit", "是四个单词但匹配 Q2");

        bindMap.forEach((bk, bv) -> {
            try {
                channel.basicPublish(exchange_name, bk, null, bv.getBytes(StandardCharsets.UTF_8));
                System.out.println(TopicEmitLog.class.getSimpleName() + "发送消息: " + bv);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }
}
