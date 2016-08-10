package com.kuparts.dubbotcc.core.cache.mongo;

import com.kuparts.dubbotcc.commons.api.TccServicePoint;
import com.kuparts.dubbotcc.commons.api.Transaction;
import com.kuparts.dubbotcc.commons.emuns.TransactionStatus;
import com.kuparts.dubbotcc.core.cache.TransactionConverter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * mongo存入数据封装
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class MongoTransactionConverter extends TransactionConverter<MongoTransactionCache> {

    @Override
    public MongoTransactionCache convertToCache(Transaction transaction) throws Exception {
        MongoTransactionCache cache = new MongoTransactionCache();
        cache.setStatus(transaction.getStatus().name());
        cache.setTransId(transaction.getTransId());
        cache.setStartTime(transaction.getStartTime());
        byte[] bytes = serializer.serialize(transaction.getPotins());
        cache.setContents(bytes);
        return cache;
    }

    @Override
    public Transaction convertByCache(MongoTransactionCache cache) throws Exception {
        TransactionStatus status = TransactionStatus.valueOf(cache.getStatus());
        List<TccServicePoint> list = serializer
                .deSerialize(cache.getContents(), CopyOnWriteArrayList.class);
        return new Transaction(
                cache.getTransId(),
                cache.getStartTime(),
                status, list);
    }

}
