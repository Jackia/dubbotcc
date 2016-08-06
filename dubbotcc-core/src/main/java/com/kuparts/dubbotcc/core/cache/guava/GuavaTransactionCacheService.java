package com.kuparts.dubbotcc.core.cache.guava;

import com.google.common.cache.LoadingCache;
import com.kuparts.dubbotcc.core.cache.DefaultTransactionConverter;
import com.kuparts.dubbotcc.core.cache.TransactionCacheService;
import com.kuparts.dubbotcc.core.cache.TransactionConverter;

import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/**
 * 使用google guava缓存...
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class GuavaTransactionCacheService implements TransactionCacheService {
    /**
     * 缓存数据
     */
    LoadingCache<String,DefaultTransactionConverter> cache;
    @Override
    public Supplier<TransactionConverter> get(String transId) {
        try {
            cache.get(transId);
        } catch (ExecutionException e) {

        }
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
