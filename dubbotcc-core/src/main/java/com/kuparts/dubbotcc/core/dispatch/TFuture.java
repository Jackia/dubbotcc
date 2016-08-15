package com.kuparts.dubbotcc.core.dispatch;

/**
 * 调用结果信息
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public interface TFuture {
    /**
     * 是否成功
     *
     * @return
     */
    boolean isSuccessfully();

    Throwable cause();

}
