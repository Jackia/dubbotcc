package com.kp.dubbotcc.core.major;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kp.dubbotcc.api.Transaction;
import com.kp.dubbotcc.commons.emuns.ServicePointStatus;
import com.kp.dubbotcc.commons.emuns.TransactionStatus;
import com.kp.dubbotcc.commons.utils.Assert;
import com.kp.dubbotcc.core.rollback.RollbackService;
import com.kp.dubbotcc.core.service.TccServicePointService;
import com.kp.dubbotcc.core.service.TransactionService;
import com.kp.dubbotcc.core.service.TransactionServiceAware;
import com.kp.dubbotcc.core.spring.BeanUtils;

/**
 * 事务管理器
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public enum TransactionManager {

    INSTANCE;
    /**
     * 线程事务
     */
    private final ThreadLocal<Transaction> localTransaction = new ThreadLocal<>();
    /**
     * 事务存入数据库
     */
    private final TransactionService service = new TransactionServiceAware();

    private final TccServicePointService tccService = new TccServicePointService();
    /**
     * 打印日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(TransactionManager.class);

    /**
     * 开始事务
     */
    public Transaction begin() {
        //开始事务
        Transaction entity = localTransaction.get();
        if (entity == null) {
            //开始事务
            entity = new Transaction();
        }
        localTransaction.set(entity);
        service.saveTransaction(entity);
        LOG.debug("begin transaction :" + entity.getTransId());
        return entity;
    }

    /**
     * 提交事务
     */
    public void commit(Transaction transaction) {
        Assert.notNull(transaction);
        transaction.modifyStatus(TransactionStatus.COMMIT);//事务状态
        tccService.modifyCurrentStatus(transaction, ServicePointStatus.SUCCESS);//节点状态
        localTransaction.set(transaction);//保存线程事务管理
        service.updateTransaction(transaction);
        LOG.debug("commit transaction :" + transaction.getTransId());
    }

    /**
     * 回滚事务
     */
    public void rollback() {
        Transaction transaction = localTransaction.get();
        if (transaction == null) {
            return;
        }
        transaction.modifyStatus(TransactionStatus.ROLLBACK);
        tccService.modifyCurrentStatus(transaction, ServicePointStatus.FAILURE);//节点状态
        BeanUtils.getInstance().getBean(RollbackService.class).submit(transaction);
        LOG.debug("submit rollback transaction :" + transaction.getTransId());
    }

    /**
     * 获取当前的事务管理
     *
     * @return 当前事务
     */
    public Transaction getTransaction() {
        return localTransaction.get();
    }

    public TransactionService getTransactionService() {
        return service;
    }
}
