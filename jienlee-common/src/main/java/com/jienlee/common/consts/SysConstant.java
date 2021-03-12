package com.jienlee.common.consts;

/**
 * @author lijian
 */
public interface SysConstant {

    Integer SYSTEM_ERROR_CODE = 10000;
    String SYSTEM_ERROR_MSG = "哎呀，网络出了小差儿，请稍后重试～";

    Integer SYSTEM_BUSY_CODE = 10001;
    String SYSTEM_BUSY_CODE_MSG = "前方道路拥堵中,请稍后重试...";

    Integer PARAMETER_ERROR_CODE = 10002;
    String PARAMETER_ERROR_MSG = "参数错误哟！请检查您的参数是否正确~";
}
