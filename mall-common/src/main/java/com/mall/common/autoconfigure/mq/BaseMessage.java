package com.mall.common.autoconfigure.mq;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
public abstract class BaseMessage {
    /**
     * 业务键，用于RocketMQ控制台查看消费情况
     */
    protected String key;

    /**
     * 发送消息来源，用于排查问题
     */
    protected String source = "";

    /**
     * 发送时间
     */
    protected String sendTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());

    /**
     * 跟踪id，用于slf4j等日志记录跟踪id，方便查询业务链
     */
    protected String traceId = UUID.randomUUID().toString();

    /**
     * 重试次数，用于判断重试次数，超过重试次数发送异常警告
     */
    protected Integer retryTimes = 0;
}
