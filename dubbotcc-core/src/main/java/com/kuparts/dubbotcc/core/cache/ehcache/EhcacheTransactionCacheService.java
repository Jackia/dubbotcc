package com.kuparts.dubbotcc.core.cache.ehcache;

import com.kuparts.dubbotcc.commons.api.Transaction;
import com.kuparts.dubbotcc.core.cache.AbstractTransactionCacheService;

/**
 * 用ehcache 实现事务缓存
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class EhcacheTransactionCacheService extends AbstractTransactionCacheService {

    @Override
    public Transaction get(String transId) {
        return null;
    }

    @Override
    public void save(Transaction transaction) {

    }

    @Override
    public void update(Transaction transaction) {

    }

    @Override
    public void remove(String transId) {

    }

    @Override
    protected String getCacheName() {
        return null;
    }

    @Override
    protected void init0(Object obj) {

    }
}
