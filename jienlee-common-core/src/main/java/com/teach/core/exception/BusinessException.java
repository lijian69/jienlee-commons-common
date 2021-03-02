package com.teach.core.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 业务异常处理类
 * @author lijian
 */
@Data
@NoArgsConstructor
public class BusinessException extends RuntimeException {

    private Integer code;
    private String msg;

    public BusinessException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
