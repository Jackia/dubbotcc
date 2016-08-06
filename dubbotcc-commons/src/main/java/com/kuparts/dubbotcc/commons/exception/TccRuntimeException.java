package com.kuparts.dubbotcc.commons.exception;

/**
 * tcc 服务运行时异常抛出.
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TccRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -1949770547060521702L;

    public TccRuntimeException() {
    }

    public TccRuntimeException(String message) {
        super(message);
    }

    public TccRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TccRuntimeException(Throwable cause) {
        super(cause);
    }
}
