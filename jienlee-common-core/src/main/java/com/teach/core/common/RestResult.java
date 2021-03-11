package com.teach.core.common;

import com.alibaba.fastjson.JSON;
import com.teach.core.consts.SysConstant;
import com.teach.core.enums.IBaseResultCode;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 同意的返回类
 *
 * @author lijian
 */
@Data
public class RestResult<T> {

    private Integer code;

    private String message;

    private T data;

    public RestResult() {
    }

    public RestResult<T> busy() {
        setCode(SysConstant.SYSTEM_BUSY_CODE);
        setMessage(SysConstant.SYSTEM_BUSY_CODE_MSG);
        return this;
    }

    public RestResult<T> success() {
        setCode(NumberUtils.INTEGER_ZERO);
        return this;
    }

    public RestResult<T> success(T data) {
        setCode(NumberUtils.INTEGER_ZERO);
        setData(data);
        return this;
    }

    public RestResult<T> paramError() {
        setCode(SysConstant.PARAMETER_ERROR_CODE);
        setMessage(SysConstant.PARAMETER_ERROR_MSG);
        return this;
    }

    public RestResult<T> failed(Integer code, String message) {
        setCode(code);
        setMessage(message);
        return this;
    }

    public RestResult<T> failed(IBaseResultCode resultCode) {
        setCode(resultCode.getCode());
        setMessage(resultCode.getMessage());
        return this;
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }
}
