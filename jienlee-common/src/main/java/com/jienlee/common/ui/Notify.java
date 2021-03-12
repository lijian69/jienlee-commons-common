package com.jienlee.common.ui;

import lombok.Builder;
import lombok.Data;

/**
 * @author lijian
 */
@Data
@Builder
public class Notify {

    private String type;

    /**
     * 消息 \n 换行
     */
    private String message;

    private Integer duration;

    private String color;

    /**
     * 顶部距离
     */
    private Integer top = 0;

    private String background;
}
