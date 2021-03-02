package com.teach.core.exception;

import com.teach.core.enums.IBaseResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 业务异常处理类
 * @author lijian
 */
@Data
public class BusinessException extends RuntimeException {

    private Integer code;
    private String message;

    public BusinessException() {
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public static BusinessException of(IBaseResultCode baseResultCode) {
        BusinessException businessException = new BusinessException(baseResultCode.getCode(),
                baseResultCode.getMessage());
        return businessException;
    }
}
