package com.kp.dubbotcc.core.service;

import com.kp.dubbotcc.core.Transaction;

/**
 * 对transService服务实现
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TransactionService {
    /**
     * 根据事务ID获取transId
     *
     * @param transId 已经存在的transId
     * @return 封装后的数据
     */
    public Transaction getServicePointByTransId(String transId) {
        return new Transaction();
    }
}
