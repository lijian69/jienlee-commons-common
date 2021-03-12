package com.jienlee.rocketmq.properties;

import lombok.Data;

import java.util.List;

@Data
public class ConsumerConfig {
    private String instanceName;
    private List<String> subscribe;
}
