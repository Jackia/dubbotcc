package com.kuparts.dubbotcc.core.rollback;

import com.kuparts.dubbotcc.api.Transaction;

/**
 * 回滚命令定义
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public interface Action {
    /**
     * 封装不同的命令执行器
     *
     * @param transaction 事务
     * @return 成功 true 失入
     */
    boolean execute(Transaction transaction);
}
