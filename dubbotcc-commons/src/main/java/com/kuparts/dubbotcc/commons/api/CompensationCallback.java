package com.kuparts.dubbotcc.commons.api;

/**
 * 处理结果异步回调
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public interface CompensationCallback {
    /**
     * 获取异步补偿后的信息
     *
     * @param response 信息
     */
    void callback(TccResponse response);
}
