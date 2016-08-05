package com.kp.dubbotcc.core.service;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kp.dubbotcc.commons.exception.TccExecption;
import com.kp.dubbotcc.commons.exception.TccRuntimeException;
import com.kp.dubbotcc.commons.utils.Assert;
import com.kp.dubbotcc.core.api.Transaction;
import com.kp.dubbotcc.core.cache.TransactionCacheService;
import com.kp.dubbotcc.core.cache.TransactionConverter;
import com.kp.dubbotcc.core.spring.BeanUtils;

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
    private TransactionCacheService cacheService;

    /**
     * 初始化一下Bean信息
     */
    public TransactionServiceAware() {
        cacheService = BeanUtils.getInstance().getBean(TransactionCacheService.class);
    }

    @Override
    public Transaction getTransactionByTransId(String transId) {
        Assert.notNull(transId);
        Supplier<TransactionConverter> supplier = cacheService.get(transId);
        Transaction transaction = null;
        try {
            transaction = supplier.get().convertByCache();
        } catch (TccExecption tccExecption) {
            LOG.error(tccExecption.getCause());
            throw new TccRuntimeException(tccExecption.getCause());
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
