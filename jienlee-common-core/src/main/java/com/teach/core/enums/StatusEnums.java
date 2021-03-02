package com.teach.core.enums;


/**
 * 状态枚举
 * @author lijian
 */
public enum StatusEnums {
    NEW(1, "新建"),
    SUBMIT(2, "提交"),
    REJECT(3, "被驳回"),
    PASS(4, "通过"),
    ;
    private Integer key;
    private String desc;

    StatusEnums(Integer key, String desc) {
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
