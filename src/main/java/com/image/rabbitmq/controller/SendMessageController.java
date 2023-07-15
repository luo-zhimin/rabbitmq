package com.image.rabbitmq.controller;

import com.image.rabbitmq.config.DelayedQueueConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.<br/>
 * Producer
 *
 * @Author : 镜像
 * @create 2023/7/13 23:00
 */
@RequestMapping("ttl")
@RestController
@Slf4j
@Api(tags = "生产者消息发送(死信/延时队列)")
public class SendMessageController {

    @Autowired
    private RabbitTemplate template;

    @GetMapping("/send/{message}")
    @ApiOperation("发送消息(普通)")
    public void sendMessage(@PathVariable(value = "message") String message) {
        log.info("当前时间：{},发送一条信息给两个 TTL 队列 :{}", new Date(), message);
        template.convertAndSend("X", "XA", "消息来自ttl为10s的队列" + message);
        template.convertAndSend("X", "XB", "消息来自ttl为40s的队列" + message);
    }

    @GetMapping("/send/{message}/{ttl}")
    @ApiOperation("发送消息(延时/普通)")
    public void sendMessage(@PathVariable(value = "message") String message, @PathVariable String ttl) {
        log.info("当前时间：{},发送一条时长:{}毫秒TTl信息给队列消息:{}", new Date(), ttl, message);

        template.convertAndSend("X", "XC", "消息: " + message, (msg) -> {
            //发送消息的时候 延迟时长
            msg.getMessageProperties().setExpiration(ttl);
            return msg;
        });
    }

    //发送消息 基于插件 消息 延迟时间
    @GetMapping("/delay/{message}/{delayTime}")
    @ApiOperation("发送消息(延时基于插件-plugins)")
    public void sendMessage(@PathVariable(value = "message") String message, @PathVariable Integer delayTime) {
        log.info("当前时间：{},发送一条时长:{}毫秒,TTl信息给延迟队列(delayed.queue:{})", new Date(), delayTime, message);

        template.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE, DelayedQueueConfig.DELAYED_ROUTING_KEY, "消息: " + message, (msg) -> {
            //发送消息的时候 延迟时长 ms
            msg.getMessageProperties().setDelay(delayTime);
            return msg;
        });
    }
}
