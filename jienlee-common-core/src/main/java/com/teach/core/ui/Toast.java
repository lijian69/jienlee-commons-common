package com.teach.core.ui;

import lombok.Builder;
import lombok.Data;

/**
 * @author lijian
 */
@Data
@Builder
public class Toast {
    private String type = "fail";
    /**
     * top middle bottom
     */
    private String position = "middle";

    private String message = "系统繁忙，请稍后重试";

    private Integer duration = 2000;
}
