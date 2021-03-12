package com.jienlee.common.exception;

import lombok.Data;

/**
 * 系统异常处理类
 *
 * @author lijian
 */
@Data
public class SystemException extends RuntimeException {

    private Integer code = 500;
    private String message;

    public SystemException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public SystemException(Integer code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
        this.message = message;
    }
}
