package com.teach.core.exception;

import com.teach.core.enums.IBaseResultCode;
import lombok.Data;

/**
 * 业务异常处理类
 *
 * @author jien.lee
 */
@Data
public class BusinessException extends RuntimeException {

    private Integer code = 500;
    private String message;

    public BusinessException() {
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(Integer code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
        this.message = message;
    }

    public static BusinessException of(IBaseResultCode baseResultCode) {
        BusinessException businessException = new BusinessException(baseResultCode.getCode(),
                baseResultCode.getMessage());
        return businessException;
    }
}
