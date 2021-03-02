package com.teach.core.enums;


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
