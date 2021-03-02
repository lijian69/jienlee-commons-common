package com.teach.core.util;

import org.springframework.lang.Nullable;

/**
 * @author jien.lee
 */
public class AssertUtil {

    public static void isTrue(boolean expression, RuntimeException runtimeException) {
        if (!expression) {
            throw runtimeException;
        }
    }

    public static void isFalse(boolean expression, RuntimeException runtimeException) {
        if (expression) {
            throw runtimeException;
        }
    }

    public static void isNull(@Nullable Object object, RuntimeException runtimeException) {
        if (object != null) {
            throw runtimeException;
        }
    }

    public static void nonNull(@Nullable Object object, RuntimeException runtimeException) {
        if (object == null) {
            throw runtimeException;
        }
    }

}
