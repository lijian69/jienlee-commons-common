package com.rocketmq.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.rocketmq.starter.properties.RocketMqProperties.PREFIX;

/**
 * @author jien.lee
 */
@Data
@ConfigurationProperties(prefix = PREFIX)
public class RocketMqProperties {
    public static final String PREFIX = "commons.rocketmq";

    private String namesrvAddr;
    private String instanceName;
    private String clientIP;
    private ProducerConfig producer;
    private ConsumerConfig consumer;

}
