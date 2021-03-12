package com.jienlee.common.enums;


/**
 * @author jien.lee
 */
public interface IBaseResultCode {

    /**
     * code
     * @return {@link Integer}
     */
    Integer getCode();

    /**
     * message
     * @return {@link String}
     */
    String getMessage();

    Integer SUCCESS_CODE = 0;

}
