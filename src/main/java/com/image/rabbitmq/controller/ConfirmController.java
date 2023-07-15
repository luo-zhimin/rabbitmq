package com.image.rabbitmq.controller;

import com.image.rabbitmq.config.ConfirmConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/7/15 21:33
 */
@Slf4j
@RestController
@RequestMapping("/confirm")
@Api(tags = "发布确认")
public class ConfirmController {

    @Autowired
    private RabbitTemplate template;

    @ApiOperation("发布确认消息")
    @GetMapping("send/{message}")
    public void sendMessage(@PathVariable String message) {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());
        //表示发送到交换机但未成功路由到任何队列的返回消息
//        correlationData.setReturned(new ReturnedMessage());

        //三种情况 测试 2.confirms[exchange error]->backup exchange 3.queue error[backup>return]

        //correct
        template.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE,
                ConfirmConfig.CONFIRM_ROUTING_KEY,
                message,
                correlationData);

        log.info("发送消息的内容:{},routingKey:{}", message, ConfirmConfig.CONFIRM_ROUTING_KEY);

        //test confirm callback (exchange update) but(queue update queue not receive)
        template.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE + "1",
                ConfirmConfig.CONFIRM_ROUTING_KEY,
                message,
                correlationData);

        log.info("发送消息的内容:{},交换机不匹配，routingKey:{}", message, ConfirmConfig.CONFIRM_ROUTING_KEY);

        //routing key error
        template.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE,
                ConfirmConfig.CONFIRM_ROUTING_KEY + "1",
                message,
                correlationData);

        log.info("发送消息的内容:{},routingKey:{}", message, ConfirmConfig.CONFIRM_ROUTING_KEY + "1");
    }
}
