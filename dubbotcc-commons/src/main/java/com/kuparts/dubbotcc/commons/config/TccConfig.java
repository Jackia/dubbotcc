package com.kuparts.dubbotcc.commons.config;


import com.kuparts.dubbotcc.commons.api.CompensationCallback;

import java.util.List;

/**
 * 需要支持的配置信息.
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TccConfig {
    /**
     * dubbo客户端名称
     */
    private String application;
    /**
     * zookeeper地址
     */
    private String zookurl;
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
    private List<CompensationCallback> callbacks;


    //get() set() method ==========


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

    public void setApplication(String application) {
        this.application = application;
    }

    public void setZookurl(String zookurl) {
        this.zookurl = zookurl;
    }

    public String getApplication() {
        return application;
    }

    public String getZookurl() {
        return zookurl;
    }
}
