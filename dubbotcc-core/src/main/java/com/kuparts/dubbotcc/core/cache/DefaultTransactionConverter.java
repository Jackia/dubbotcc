package com.kuparts.dubbotcc.core.cache;

import com.kuparts.dubbotcc.api.Transaction;
import com.kuparts.dubbotcc.commons.exception.TccException;

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
    public TransactionCache convertToCache() throws TccException {
        TransactionCache cache = new TransactionCache();
        cache.setTransId(getTransaction().getTransId());
        byte[] bytes = getSerializer().serialize(getTransaction());
        cache.setContents(bytes);
        return cache;
    }

    @Override
    public Transaction convertByCache() throws TccException {
        return getSerializer().deSerialize(getTransactionCache().getContents(), Transaction.class);
    }
}
