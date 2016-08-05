package com.kp.dubbotcc.core.config;

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
     * 使用的表名
     */
    private String tableName;

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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
