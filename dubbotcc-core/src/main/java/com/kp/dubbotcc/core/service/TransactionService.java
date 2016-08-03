package com.kp.dubbotcc.core.service;

import com.alibaba.dubbo.common.extension.SPI;
import com.kp.dubbotcc.core.Transaction;
import com.sun.istack.internal.NotNull;

/**
 * 服务存入实现
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@SPI
public interface TransactionService {

    /**
     * 根所事务id获取transId对象
     * @param transId 生成的事务id
     * @return trans对象
     */
    Transaction getTransactionByTransId(@NotNull String transId);
}

