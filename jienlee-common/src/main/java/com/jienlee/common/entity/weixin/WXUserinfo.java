package com.jienlee.common.entity.weixin;


import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class WXUserinfo implements Serializable {

    private String unionid;
    private String openid;
    private String nickname;
    private Integer sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private List<String> privilege;
    private String errcode;
    private String errmsg;
}

