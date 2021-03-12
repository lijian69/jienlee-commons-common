package com.jienlee.common.enums;

/**
 * 角色
 * @author lijian
 */
public enum RoleEnum {
    USER(1, "用户端"),
    TEACHER(2, "教师端"),
    ;

    private Integer key;
    private String desc;

    RoleEnum(Integer key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public Integer getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

}
