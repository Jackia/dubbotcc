package com.kuparts.dubbotcc.core.dispatch.support;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public interface NotifyCallback {
    int SUCCESS = 0;//成功
    int FAILURE = 1;//失败
    int UNKNOWN = 2;//未知

    /**
     * 通知回财
     *
     * @param status    返回状态
     * @param throwable 错误信息,如果有就返回
     */
    void done(int status, Throwable throwable);
}
