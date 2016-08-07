package com.kuparts.dubbotcc.core.major;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.api.Transaction;
import com.kuparts.dubbotcc.commons.emuns.ServicePointStatus;
import com.kuparts.dubbotcc.commons.emuns.TransactionStatus;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.core.rollback.RollbackService;
import com.kuparts.dubbotcc.core.service.TccServicePointService;
import com.kuparts.dubbotcc.core.service.TransactionService;
import com.kuparts.dubbotcc.core.service.TransactionServiceAware;

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
    private final ThreadLocal<Transaction> LOCAL = new ThreadLocal<>();
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
        Transaction entity = LOCAL.get();
        if (entity == null) {
            //开始事务
            entity = new Transaction();
        }
        StackTraceElement stack = Thread.currentThread().getStackTrace()[2];
        entity.setCallback(stack.getMethodName());
        LOCAL.set(entity);
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
        LOCAL.set(transaction);//保存线程事务管理
        service.updateTransaction(transaction);
        LOG.debug("commit transaction :" + transaction.getTransId());
    }

    /**
     * 回滚事务
     */
    public void rollback() {
        Transaction transaction = LOCAL.get();
        if (transaction == null) {
            return;
        }
        transaction.modifyStatus(TransactionStatus.ROLLBACK);
        tccService.modifyCurrentStatus(transaction, ServicePointStatus.FAILURE);//节点状态
        BeanServiceUtils.getInstance().getBean(RollbackService.class).submit(transaction);
        LOG.debug("submit rollback transaction :" + transaction.getTransId());
    }

    /**
     * 获取当前的事务管理
     *
     * @return 当前事务
     */
    public Transaction getTransaction() {
        return LOCAL.get();
    }

    public TransactionService getTransactionService() {
        return service;
    }
}
