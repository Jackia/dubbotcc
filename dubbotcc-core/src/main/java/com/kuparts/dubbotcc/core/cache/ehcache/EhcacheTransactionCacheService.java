package com.kuparts.dubbotcc.core.cache.ehcache;

import com.kuparts.dubbotcc.core.cache.TransactionCacheService;
import com.kuparts.dubbotcc.core.cache.TransactionConverter;

import java.util.function.Supplier;

/**
 * 用ehcache 实现事务缓存
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class EhcacheTransactionCacheService implements TransactionCacheService {
    @Override
    public Supplier<TransactionConverter> get(String transId) {
        return null;
    }

    @Override
    public void save(Supplier<? extends TransactionConverter> convert) {

    }

    @Override
    public void update(Supplier<? extends TransactionConverter> convert) {

    }

    @Override
    public void remove(String transId) {

    }
}
