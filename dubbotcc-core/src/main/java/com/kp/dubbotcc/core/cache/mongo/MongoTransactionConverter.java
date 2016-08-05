package com.kp.dubbotcc.core.cache.mongo;

import com.kp.dubbotcc.commons.emuns.TransactionStatus;
import com.kp.dubbotcc.commons.exception.TccExecption;
import com.kp.dubbotcc.core.api.Transaction;
import com.kp.dubbotcc.core.cache.TransactionConverter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * project：dubbotcc-parent /www.kuparts.com
 * Created By chenbin on 2016/8/4 13:14
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class MongoTransactionConverter extends TransactionConverter<MongoTransactionCache> {


    @Override
    public MongoTransactionCache convertToCache() throws TccExecption {
        MongoTransactionCache cache = new MongoTransactionCache();
        cache.setStatus(this.getTransaction().getStatus().name());
        cache.setTransId(this.getTransaction().getTransId());
        cache.setStartTime(this.getTransaction().getStartTime());
        byte[] bytes = this.getSerializer().serialize(this.getTransaction().getPotins());
        cache.setContents(bytes);
        return cache;
    }

    @Override
    public Transaction convertByCache() throws TccExecption {
        MongoTransactionCache cache = this.getTransactionCache();
        TransactionStatus status = TransactionStatus.valueOf(cache.getStatus());
        List list = this.getSerializer().deSerialize(cache.getContents(), CopyOnWriteArrayList.class);
        return new Transaction(
                cache.getTransId(),
                cache.getStartTime(),
                status, list);
    }
}
