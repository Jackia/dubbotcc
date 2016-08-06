package com.kp.dubbotcc.core.cache.mongo;

import com.kp.dubbotcc.core.cache.TransactionCache;
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
