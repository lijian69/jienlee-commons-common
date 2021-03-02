package com.teach.core.enums.ui;

/**
 * @author lijian
 */

public enum PositionEnum {
    TOP("top"),
    MIDDLE("middle"),
    BOTTOM("bottom"),

    ;
    private String value;

    PositionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
