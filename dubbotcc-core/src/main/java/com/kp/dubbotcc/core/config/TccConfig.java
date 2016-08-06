package com.kp.dubbotcc.core.config;

import com.kp.dubbotcc.api.CompensationCallback;

import java.io.Serializable;

/**
 * 需要支持的配置信息.
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TccConfig implements Serializable {
    /**
     * 提供不同的序列化对象
     */
    private String serializer;
    /**
     * 提供不同的事务存储对象
     */
    private String cache;
    /**
     * 补偿方法结果回调
     */
    private transient CompensationCallback callback;

    public String getSerializer() {
        return serializer;
    }

    public void setSerializer(String serializer) {
        this.serializer = serializer;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    public void setCallback(CompensationCallback callback) {
        this.callback = callback;
    }

    public CompensationCallback getCallback() {
        return callback;
    }
}
