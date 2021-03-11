package com.teach.core.exception;

import lombok.Data;

/**
 * 系统异常处理类
 *
 * @author lijian
 */
@Data
public class CheckException extends RuntimeException {

    private String message;

    public CheckException() {
    }

    public CheckException(String message) {
        super(message);
        this.message = message;
    }

    public CheckException(String message, Throwable throwable) {
        super(message, throwable);
        this.message = message;
    }

    public static CheckException of(String message) {
        return new CheckException(message);
    }
}
