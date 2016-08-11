package com.kuparts.dubbotcc.supervise.net;

/**
 * 服务客户端管理
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public interface NetClient {
    /**
     * 异步调用
     */
    void asynInvoker();

    /**
     * 同步调用
     */
    void synInvoker();
}