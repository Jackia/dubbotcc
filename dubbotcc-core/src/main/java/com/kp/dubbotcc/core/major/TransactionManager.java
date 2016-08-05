package com.kp.dubbotcc.core.major;

import com.kp.dubbotcc.commons.utils.Assert;
import com.kp.dubbotcc.core.api.Transaction;
import com.kp.dubbotcc.core.service.TransactionService;
import com.kp.dubbotcc.core.service.TransactionServiceAware;

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
        return entity;
    }

    /**
     * 提交事务
     */
    public void commit(Transaction transaction) {
        //表示只有一个节点,在开始事务阶段已提交,则这里不需要提交
        if (transaction.getPotins().size() <= 1) {
            return;
        }
        Assert.notNull(transaction);
        localTransaction.set(transaction);//保存线程事务管理
        service.updateTransaction(transaction);
    }

    /**
     * 回滚事务
     */
    public void rollback() {
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
