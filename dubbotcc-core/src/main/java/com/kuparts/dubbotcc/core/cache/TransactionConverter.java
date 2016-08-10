package com.kuparts.dubbotcc.core.cache;


import com.kuparts.dubbotcc.api.Transaction;
import com.kuparts.dubbotcc.core.serializer.ObjectSerializer;

/**
 * 将事务对象转换为存入缓存对象
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public abstract class TransactionConverter<C extends TransactionCache> {

    protected ObjectSerializer serializer;

    protected ObjectSerializer getSerializer() {
        return serializer;
    }

    public void setSerializer(ObjectSerializer serializer) {
        this.serializer = serializer;
    }

    /**
     * 将transaction对象转换为可存入缓存对象
     *
     * @return
     */
    public abstract C convertToCache(Transaction transaction) throws Exception;

    public abstract Transaction convertByCache(C cache) throws Exception;
}
