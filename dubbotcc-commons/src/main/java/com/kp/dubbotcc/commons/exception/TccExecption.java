package com.kp.dubbotcc.commons.exception;

/**
 * tcc 服务编译时异常
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TccExecption extends Exception {
    public TccExecption() {
    }

    public TccExecption(String message) {
        super(message);
    }

    public TccExecption(String message, Throwable cause) {
        super(message, cause);
    }

    public TccExecption(Throwable cause) {
        super(cause);
    }
}
