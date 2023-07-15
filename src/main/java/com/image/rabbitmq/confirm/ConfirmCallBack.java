package com.image.rabbitmq.confirm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by IntelliJ IDEA.
 * 确认回调接口
 *
 * @Author : 镜像
 * @create 2023/7/15 21:52
 */
@Component
@Slf4j
public class ConfirmCallBack implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private RabbitTemplate template;

    @PostConstruct
    public void init() {
        //注入 调取内部接口
        template.setConfirmCallback(this);
    }

    /**
     * Confirmation callback.
     * 交换机确认回调方法
     * 1.发消息 交换机收到了 回调
     * 1.1 correlationData 保存回调消息的id及相关信息
     * 1.2 交换机收到消息 ack -> true
     * 1.3 cause null
     * 2.发消息 交换机失败了 回调
     * 2.1 交换机收到消息 ack -> false
     * 2.2 cause 失败的原因
     *
     * @param correlationData correlation data for the callback.
     * @param ack             true for ack, false for nack
     * @param cause           An optional cause, for nack, when available, otherwise null.
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("交换机收到了消息，id为：({})的消息", correlationData.getId());
            return;
        }
        log.info("交换机还为收到消息，id为：{}的消息，原因为：({})", correlationData.getId(), cause);
    }
}
