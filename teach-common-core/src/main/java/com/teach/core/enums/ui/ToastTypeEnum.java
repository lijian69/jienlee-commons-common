package com.teach.core.enums.ui;

/**
 * @author lijian
 */

public enum ToastTypeEnum {
    LOADING("loading"),
    SUCCESS("success"),
    FAIL("fail"),
    HTML("html"),
    ;

    private String value;

    ToastTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
