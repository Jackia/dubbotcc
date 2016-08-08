package com.kuparts.dubbotcc.core.service;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.api.Transaction;
import com.kuparts.dubbotcc.commons.exception.TccException;
import com.kuparts.dubbotcc.commons.exception.TccRuntimeException;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.core.cache.TransactionCacheService;
import com.kuparts.dubbotcc.core.cache.TransactionConverter;
import com.kuparts.dubbotcc.core.major.BeanServiceUtils;

import java.util.function.Supplier;

/**
 * 对transService服务实现
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TransactionServiceAware implements TransactionService {
    /**
     * 打印日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(TransactionServiceAware.class);
    /**
     * 获取对象
     */
    private final TransactionCacheService cacheService;

    /**
     * 初始化一下Bean信息
     */
    public TransactionServiceAware() {
        cacheService = BeanServiceUtils.getInstance().getBean(TransactionCacheService.class);
    }

    @Override
    public Transaction getTransactionByTransId(String transId) {
        Assert.notNull(transId);
        Supplier<TransactionConverter> supplier = cacheService.get(transId);
        Transaction transaction;
        try {
            transaction = supplier.get().convertByCache();
        } catch (Exception tccException) {
            LOG.error(tccException.getMessage());
            throw new TccRuntimeException(tccException.getCause());
        }
        return transaction;
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        Assert.notNull(transaction);
        cacheService.save(() -> cacheService.initConverter().initToCache(transaction));
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        Assert.notNull(transaction);
        Assert.notNull(transaction.getTransId());
        cacheService.update(() -> cacheService.initConverter().initToCache(transaction));
    }
}
