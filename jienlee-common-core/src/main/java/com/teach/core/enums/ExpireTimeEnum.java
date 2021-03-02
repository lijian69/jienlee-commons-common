package com.teach.core.enums;

/**
 * @author jien.lee
 */

public enum ExpireTimeEnum {

    MONTH_1(2592000),
    MONTH_2(5184000),
    WEEK_1(604800),
    DAY_1(86400),
    DAY_2(172800),
    DAY_3(259200),
    HOUR_1(3600),
    HOUR_2(7200),
    HOUR_8(28800),
    MINUTE_10(600),
    MINUTE_30(1800),
    MINUTE_35(2100),
    MINUTE_45(2700),
    SECONDS_5(5),
    ;

    int value;
    ExpireTimeEnum(int i) {
        this.value = i;
    }
    public int getValue(){
        return value;
    }

}
