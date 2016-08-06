package com.kuparts.dubbotcc.commons.utils;

import com.kuparts.dubbotcc.commons.exception.TccRuntimeException;

public class Assert {

    private Assert() {

    }

    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new TccRuntimeException(message);
        }
    }

    public static void notNull(Object obj) {
        if (obj == null) {
            throw new TccRuntimeException("argument invalid,Please check");
        }
    }

    public static void checkConditionArgument(boolean condition, String message) {
        if (!condition) {
            throw new TccRuntimeException(message);
        }
    }

}
