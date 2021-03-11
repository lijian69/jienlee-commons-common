package com.teach.core.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.TreeMap;

/**
 * 参数签名工具
 * @author jien.lee
 */
@Slf4j
public class SignUtil {

    /**
     * 生成调用 rest 接口的参数签名
     * @param params 请求将要传递的参数
     * @param clientId 使用的 clientId
     * @param signKey clientId 对应的 signKey
     * @return 完整的签名串 {sign}+" "+timestamp+" "+clientId，直接设置到请求 Header 中即可
     */
    public static String sign(TreeMap<String, String> params, String clientId, String signKey){
        StringBuffer sortedParams = new StringBuffer();
        if(params != null){
            for(Map.Entry entry : params.entrySet()){
                sortedParams.append("&");
                sortedParams.append(entry.getKey());
                sortedParams.append("=");
                sortedParams.append(entry.getValue());
            }
            sortedParams = new StringBuffer(sortedParams.substring(1));
        }
        long ts = System.currentTimeMillis() / 1000;
        sortedParams.append(String.format("&timestamp=%s&sign_key=%s", ts, signKey));
        String sign = String.format("%s %s %s", EncryptUtil.MD5(sortedParams.toString()), ts, clientId);
        log.debug("sortedParams: {}, sign: {}", sortedParams, sign);
        return sign;
    }

}
