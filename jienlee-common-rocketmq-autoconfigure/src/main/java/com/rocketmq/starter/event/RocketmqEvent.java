package com.rocketmq.starter.event;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.common.message.MessageExt;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.ApplicationEvent;

import java.io.UnsupportedEncodingException;

/**
 * @author jien.lee
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class RocketmqEvent extends ApplicationEvent {
    private static final long serialVersionUID = -4468405250074063206L;

    private DefaultMQPushConsumer consumer;
    private MessageExt messageExt;
    private String topic;
    private String tag;
    private byte[] body;

    public RocketmqEvent(MessageExt msg,DefaultMQPushConsumer consumer) throws Exception {
        super(msg);
        this.topic = msg.getTopic();
        this.tag = msg.getTags();
        this.body = msg.getBody();
        this.consumer = consumer;
        this.messageExt = msg;
    }

    public String getMsg() {
        try {
            return new String(this.body,"utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public String getMsg(String code) {
        try {
            return new String(this.body,code);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

}