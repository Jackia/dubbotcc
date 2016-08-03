package com.kp.dubbotcc.core.service;

import com.kp.dubbotcc.core.Transaction;
import com.sun.istack.internal.NotNull;

/**
 * 对transService服务实现
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class MongoTransactionService implements TransactionService{
    @Override
    public Transaction getTransactionByTransId(@NotNull String transId) {
        return new Transaction();
    }

}
