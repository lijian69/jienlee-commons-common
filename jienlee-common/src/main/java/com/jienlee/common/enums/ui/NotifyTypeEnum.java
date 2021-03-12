package com.jienlee.common.enums.ui;

/**
 * @author lijian
 */

public enum NotifyTypeEnum {
    PRIMARY("primary"),
    SUCCESS("success"),
    WARNING("warning"),
    ;

    private String value;

    NotifyTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
