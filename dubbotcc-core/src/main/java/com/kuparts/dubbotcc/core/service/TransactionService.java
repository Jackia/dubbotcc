package com.kuparts.dubbotcc.core.service;


import com.kuparts.dubbotcc.api.Transaction;

/**
 * 服务存入实现
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public interface TransactionService {

    /**
     * 根所事务id获取transId对象
     *
     * @param transId 生成的事务id
     * @return trans对象
     */
    Transaction getTransactionByTransId(String transId);
    /**
     * 保存事务信息
     *
     * @param transaction 事务对象
     */
    void saveTransaction(Transaction transaction);
    /**
     * 修改事务信息
     * @param transaction 事务对象
     */
    void updateTransaction(Transaction transaction);
}

