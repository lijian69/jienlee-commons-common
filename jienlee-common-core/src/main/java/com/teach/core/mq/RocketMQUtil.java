package com.teach.core.mq;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.Message;
import com.teach.core.json.JsonMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RocketMQUtil {

    private static final Logger logger = LoggerFactory.getLogger(RocketMQUtil.class);

    private RocketMQUtil() {
    }

    /**
     * 发送mq 默认mq类型hook
     *
     * @param rowKey 主键
     * @param t
     * @param topic
     */
    public static <T> void sendToMQ(String rowKey, T t, String topic) {
        sendToMQ(rowKey, t, topic, "");
    }

    /**
     * 发送数据到mq 默认mq类型hook
     *
     * @param rowKey 主键
     * @param t
     * @param topic
     * @param tags
     * @param <T>
     */
    public static <T> void sendToMQ(String rowKey, T t, String topic, final String tags) {
        sendToMQ(rowKey, t, topic, tags, RocketMQProducerHelper.EnvEnum.DEFAULT.getValue());
    }

    /**
     * 发送数据到mq -- 带mqtype
     *
     * @param rowKey 主键
     * @param t
     * @param topic
     * @param tags
     * @param <T>
     */
    public static <T> void sendToMQ(String rowKey, T t, String topic, final String tags, Integer mqType) {
        Message msg = new Message();
        msg.setTopic(topic);
        msg.setKeys(rowKey);
        if (StringUtils.isNotEmpty(tags)) {
            msg.setTags(tags);
        }
        msg.setBody(JsonMapper.toJson(t).getBytes());
        sendMessage(msg, mqType);
    }

    /**
     * 发送数据到mq 默认mq类型hook
     *
     * @param rowKey 主键
     * @param t
     * @param topic
     */
    public static void sendToMQ(byte[] t, String rowKey, String topic, String tags) {
        sendToMQ(rowKey, t, topic, tags, RocketMQProducerHelper.EnvEnum.DEFAULT.getValue());
    }

    /**
     * @param rowKey     消息key
     * @param t          消息内容
     * @param topic      消息主题
     * @param tags       消息tag
     * @param delayLevel 延迟级别 [1,28] mq集群可配置，默认从1到28级的延迟时间为
     *                   1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h 3h 4h 5h 6h 7h 8h 9h 10h 11h 12h
     * @param <T>
     */
    public static <T> void sendDelayMQ(String rowKey, T t, String topic, final String tags, int delayLevel) {
        Message msg = new Message();
        msg.setTopic(topic);
        msg.setKeys(rowKey);
        if (StringUtils.isNotEmpty(tags)) {
            msg.setTags(tags);
        }
        msg.setDelayTimeLevel(delayLevel);
        msg.setBody(JsonMapper.toJson(t).getBytes());
        sendMessage(msg, RocketMQProducerHelper.EnvEnum.DEFAULT.getValue());
    }

    /**
     * 发送数据到mq 默认mq类型hook
     *
     * @param rowKey 主键
     * @param t
     * @param topic
     */
    public static void sendToMQ(byte[] t, String rowKey, String topic, String tags, Integer mqType) {
        Message msg = new Message();
        msg.setTopic(topic);
        msg.setKeys(rowKey);
        if (StringUtils.isNotEmpty(tags)) {
            msg.setTags(tags);
        }
        msg.setBody(t);
        sendMessage(msg, mqType);
        logger.debug("Message send: body: {}", new String(t));
    }

    /**
     * 发送数据到mq 默认mq类型hook 增加可选orderby
     *
     * @param rowKey 主键
     * @param t
     * @param topic
     */
    public static void sendToMQ(byte[] t, String rowKey, String topic, String tags, Integer mqType, String orderby) {
        Message msg = new Message();
        msg.setTopic(topic);
        msg.setKeys(rowKey);
        if (StringUtils.isNotEmpty(tags)) {
            msg.setTags(tags);
        }
        msg.setBody(t);
        sendMessage(msg, mqType, orderby);
        logger.debug("Message send: body: {}", new String(t));
    }

    private static void sendMessage(final Message msg, final Integer mqType) {
        sendMessage(msg, mqType, msg.getKeys());
    }

    private static void sendMessage(final Message msg, final Integer mqType, final String orderby) {

        try {
            RocketMQProducerHelper.getInstance(mqType).hSend(msg, orderby);
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public static void sendToMQByte(String rowKey, byte[] t, String topic) {
        sendToMQByte(rowKey, t, topic, "");
    }

    public static void sendToMQByte(String rowKey, byte[] t, String topic, String tags) {
        sendToMQByte(rowKey, t, topic, tags, Integer.valueOf(RocketMQProducerHelper.EnvEnum.DEFAULT.getValue()));
    }

    public static void sendToMQByte(String rowKey, byte[] t, String topic, String tags, Integer mqType) {
        Message msg = new Message();
        msg.setTopic(topic);
        msg.setKeys(rowKey);
        if (StringUtils.isNotEmpty(tags)) {
            msg.setTags(tags);
        }
        msg.setBody(t);
        sendMessage(msg, mqType);
        logger.debug("Message send: body: {}", new String(t));
    }
}
