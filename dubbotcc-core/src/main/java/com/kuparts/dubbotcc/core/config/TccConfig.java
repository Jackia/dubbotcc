package com.kuparts.dubbotcc.core.config;

import com.kuparts.dubbotcc.api.CompensationCallback;

import java.io.Serializable;
import java.util.List;

/**
 * 需要支持的配置信息.
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TccConfig implements Serializable {
    private static final long serialVersionUID = 4374230424756301556L;
    /**
     * 提供不同的序列化对象
     */
    private String serializer;
    /**
     * 提供不同的事务存储对象
     */
    private String cache;
    /**
     * 补偿方法获取..
     */
    private transient List<CompensationCallback> callbacks;

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

    public List<CompensationCallback> getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(List<CompensationCallback> callbacks) {
        this.callbacks = callbacks;
    }
}
