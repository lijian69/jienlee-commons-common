package com.teach.core.mq;


/**
 *
 */
public class RocketMQProducerConfig {

    /**
     * default1_rocketMQNamesrvAddr
     **/
    private String default_rocketMQNamesrvAddr;

    /**
     * online_rocketMQNamesrvAddr  可直接配置线上地址
     **/
    private String online_rocketMQNamesrvAddr;

    public String getDefault_rocketMQNamesrvAddr() {
        return default_rocketMQNamesrvAddr;
    }

    public void setDefault_rocketMQNamesrvAddr(String default_rocketMQNamesrvAddr) {
        this.default_rocketMQNamesrvAddr = default_rocketMQNamesrvAddr;
    }

    public String getOnline_rocketMQNamesrvAddr() {
        return online_rocketMQNamesrvAddr;
    }

    public void setOnline_rocketMQNamesrvAddr(String online_rocketMQNamesrvAddr) {
        this.online_rocketMQNamesrvAddr = online_rocketMQNamesrvAddr;
    }

    public String getNamesrvAddr(RocketMQProducerHelper.EnvEnum envEnum) {
        switch (envEnum) {
            case ONLINE:
                return online_rocketMQNamesrvAddr;
            default:
                return default_rocketMQNamesrvAddr;
        }
    }
}
