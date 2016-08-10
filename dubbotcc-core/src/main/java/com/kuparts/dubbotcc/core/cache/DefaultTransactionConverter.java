package com.kuparts.dubbotcc.core.cache;


import com.kuparts.dubbotcc.commons.api.Transaction;

/**
 * 实现一个默认转换器..
 * 主要从事务对象,转为存储对象之间的互相转换..
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class DefaultTransactionConverter extends TransactionConverter<TransactionCache> {
    @Override
    public TransactionCache convertToCache(Transaction transaction) throws Exception {
        TransactionCache cache = new TransactionCache();
        cache.setTransId(transaction.getTransId());
        byte[] bytes = serializer.serialize(transaction);
        cache.setContents(bytes);
        return cache;
    }

    @Override
    public Transaction convertByCache(TransactionCache cache) throws Exception {
        return getSerializer().deSerialize(cache.getContents(), Transaction.class);
    }
}
