package com.image.rabbitmq.controller;

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
public class SendMessageController {

    @Autowired
    private RabbitTemplate template;

    @GetMapping("/send/{message}")
    @ApiOperation("发送消息")
    public void sendMessage(@PathVariable(value = "message") String message) {
        log.info("当前时间：{},发送一条信息给两个 TTL 队列 :{}", new Date(), message);
        template.convertAndSend("X", "XA", "消息来自ttl为10s的队列" + message);
        template.convertAndSend("X", "XB", "消息来自ttl为40s的队列" + message);
    }
}