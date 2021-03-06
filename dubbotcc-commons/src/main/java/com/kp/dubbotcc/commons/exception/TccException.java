package com.kp.dubbotcc.commons.exception;

/**
 * tcc 服务编译时异常
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TccException extends Exception {
    private static final long serialVersionUID = -948934144333391208L;

    public TccException() {
    }

    public TccException(String message) {
        super(message);
    }

    public TccException(String message, Throwable cause) {
        super(message, cause);
    }

    public TccException(Throwable cause) {
        super(cause);
    }
}
