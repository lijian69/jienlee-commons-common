package com.teach.core.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统异常处理类
 * @author lijian
 */
@Data
@NoArgsConstructor
public class SystemException extends RuntimeException {

    private Integer code;
    private String msg;

    public SystemException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
