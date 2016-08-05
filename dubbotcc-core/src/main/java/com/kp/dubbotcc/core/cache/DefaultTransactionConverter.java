package com.kp.dubbotcc.core.cache;

import com.kp.dubbotcc.commons.exception.TccExecption;
import com.kp.dubbotcc.core.api.Transaction;

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
    public TransactionCache convertToCache() throws TccExecption {
        TransactionCache cache = new TransactionCache();
        cache.setTransId(this.getTransaction().getTransId());
        byte[] bytes = this.getSerializer().serialize(this.getTransaction());
        cache.setContents(bytes);
        return cache;
    }

    @Override
    public Transaction convertByCache() throws TccExecption {
        return this.getSerializer().deSerialize(this.getTransactionCache().getContents(), Transaction.class);
    }
}
