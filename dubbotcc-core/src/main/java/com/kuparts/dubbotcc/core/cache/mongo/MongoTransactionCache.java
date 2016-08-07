package com.kuparts.dubbotcc.core.cache.mongo;

import com.kuparts.dubbotcc.core.cache.TransactionCache;
import org.bson.types.ObjectId;

/**
 * mongo扩展存储.
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class MongoTransactionCache extends TransactionCache {

    private static final long serialVersionUID = 5777966444956746722L;
    private ObjectId id;

    /**
     * 事务状态
     */
    private String status;
    /**
     * 开始时间
     */
    private Long startTime;
    /**
     * 回调方法标识
     */
    private String callback;

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
}
