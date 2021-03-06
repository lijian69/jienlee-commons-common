package com.teach.core.common;

import com.alibaba.fastjson.JSONObject;
import com.teach.core.consts.SysConstant;
import com.teach.core.ui.Toast;
import lombok.Data;

import java.util.HashMap;

/**
 * 同意的返回类
 *
 * @author lijian
 */
@Data
public class ResultMap extends HashMap<String, Object> {

    public ResultMap busy() {
        put("code", SysConstant.SYSTEM_BUSY_CODE);
        put("message", SysConstant.SYSTEM_BUSY_CODE_MSG);
        return this;
    }

    public ResultMap success() {
        put("code", 0);
        return this;
    }

    public ResultMap success(Object object) {
        put("code", 0);
        put("data", object);
        return this;
    }

    public ResultMap paramError() {
        put("code", SysConstant.PARAMETER_ERROR_CODE);
        put("message", SysConstant.PARAMETER_ERROR_MSG);
        return this;
    }

    public ResultMap failed(Integer code, String message) {
        put("code", code);
        put("message", message);
        return this;
    }

    public ResultMap showToast(Toast toast) {
        put("code", 10001);
        put("toast", toast);
        return this;
    }

    public String toJson() {
        return JSONObject.toJSONString(this);
    }
}
