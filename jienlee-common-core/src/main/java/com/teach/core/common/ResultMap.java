package com.teach.core.common;

import com.alibaba.fastjson.JSONObject;
import com.teach.core.consts.SysConstant;
import com.teach.core.ui.Toast;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 同意的返回类
 *
 * @author lijian
 */
@Data
public class ResultMap {

    private Map<String, Object> map;

    public ResultMap() {
        this.map = new HashMap<>(8);
    }

    public ResultMap put(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public ResultMap busy() {
        map.put("code", SysConstant.SYSTEM_BUSY_CODE);
        map.put("message", SysConstant.SYSTEM_BUSY_CODE_MSG);
        return this;
    }

    public ResultMap success() {
        map.put("code", 0);
        return this;
    }

    public ResultMap success(Object object) {
        map.put("code", 0);
        map.put("data", object);
        return this;
    }
    public ResultMap paramError(){
        map.put("code", SysConstant.PARAMETER_ERROR_CODE);
        map.put("message", SysConstant.PARAMETER_ERROR_MSG);
        return this;
    }

    public ResultMap failed(Integer code, String message) {
        map.put("code", code);
        map.put("message", message);
        return this;
    }

    public ResultMap showToast(Toast toast) {
        map.put("code", 10001);
        map.put("toast", toast);
        return this;
    }

    public String toJson() {
        return JSONObject.toJSONString(map);
    }
}
