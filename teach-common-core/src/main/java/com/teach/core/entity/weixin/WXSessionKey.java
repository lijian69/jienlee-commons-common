package com.teach.core.entity.weixin;

import lombok.Data;

@Data
public class WXSessionKey {

    /**
     *  用户唯一标识
     */
    private String openid;

    /**
     * 会话密钥
     */
    private String session_key;


    /**
     * 用户唯一标识
     */
    private String unionid;

    /**
     * 错误码
     */
    private int errcode;

    /**
     * 错误消息
     */
    private String errmsg;
}
