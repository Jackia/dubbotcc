package com.kp.dubbotcc.core.service;

import com.kp.dubbotcc.commons.utils.Assert;
import com.kp.dubbotcc.core.ServicePoint;
import com.kp.dubbotcc.core.Transaction;

import java.util.concurrent.ConcurrentHashMap;

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
     * 线程节点
     */
    private final ThreadLocal<ServicePoint> localPoint = new ThreadLocal<>();
    /**
     * 事务管理容器
     */
    ConcurrentHashMap<String, Transaction> pools = new ConcurrentHashMap<>();
    /**
     * 事务服务
     */
    private TransactionService transactionService;

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
        return entity;
    }

    /**
     * 提交事务
     */
    public void commit(TccServicePointTrace trace) {
        Assert.notNull(trace);
        Assert.notNull(trace.getPoint());
        Transaction tran = localTransaction.get();
        tran.addServicePotin(trace.getPoint());
        localPoint.set(trace.getPoint());
        localTransaction.set(tran);
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
        return transactionService = new TransactionService();
    }

    /**
     * 获取线程服务节点
     *
     * @return
     */
    public ServicePoint getServicePoint() {
        return localPoint.get();
    }
}
