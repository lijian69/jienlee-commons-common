package com.jienlee.common.mq;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.TransactionMQProducer;
import com.alibaba.rocketmq.common.message.Message;

/**
 * @author jien.lee
 */
public class RocketMqUtil implements ApplicationContextAware {
    private static Logger logger = LoggerFactory.getLogger(RocketMqUtil.class);

    private RocketMqUtil() {}

    private static DefaultMQProducer defaultMQProducer;

    private static TransactionMQProducer transactionMQProducer;

    public static <T> void sendMsg(String rowKey, T t, String topic) {
        sendToMQ(rowKey, t, topic, "");
    }

    public static <T> void sendToMQ(String rowKey, T t, String topic, final String tags) {
        if (t instanceof String) {
            sendMessage(rowKey, topic, (String) t, tags);
        } else if (t instanceof Object) {
            sendMessage(rowKey, topic, JSON.toJSONString(t), tags);
        }
    }

    private static <T> void sendMessage(String rowKey, String topic, String value, String tags) {
        Message message = new Message();
        message.setTopic(topic);
        message.setKeys(rowKey);
        if (StringUtils.isNotEmpty(tags)) {
            message.setTags(tags);
        }
        message.setBody(JSON.toJSONString(value).getBytes());
        try {
            defaultMQProducer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    logger.debug("send {} success!", rowKey);
                }

                @Override
                public void onException(Throwable throwable) {
                    logger.error("send {} fail!", rowKey);
                }
            });
        } catch (Exception e) {
            logger.error("defaultMQProducer sendMessage error! rowKeu:{}", rowKey, e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        defaultMQProducer = applicationContext.getBean(DefaultMQProducer.class);
        transactionMQProducer = applicationContext.getBean(TransactionMQProducer.class);
    }
}
