package com.teach.core.entity.weixin;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Jary on 2017/8/17.
 */
@Data
public class WXAccessToken implements Serializable {

    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String openid;
    private String unionid;
    private String scope;
    private String errcode;
    private String errmsg;
}
