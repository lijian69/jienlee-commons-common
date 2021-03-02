package com.rocketmq.starter.properties;

import lombok.Data;

@Data
public class ProducerConfig {

    private String instanceName;

    private String tranInstanceName;
}
