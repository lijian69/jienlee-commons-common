package com.teach.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: teach-commons-common
 * @description:
 * @author: lijian1 ( email: lijian1@didachuxing.com )
 * @create: 2021-01-11 14:09
 **/
@Data
@Component
@ConfigurationProperties(prefix = "jienlee.redis")
public class RedisProperties {

    private String host = "127.0.0.1";
    private int port = 6379;
    private String password;
    private int database = 0;

    private int maxActive = 8;
    private int maxIdle = 8;
    private int maxWait = -1;
    private int minIdle = 0;

}

