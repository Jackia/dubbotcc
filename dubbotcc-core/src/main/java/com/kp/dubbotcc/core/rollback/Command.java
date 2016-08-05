package com.kp.dubbotcc.core.rollback;

import com.kp.dubbotcc.api.Transaction;

/**
 * 回滚命令定义
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public interface Command {
    /**
     * 封装不同的命令执行器
     *
     * @param transaction 事务
     * @return 成功 true 失败 false
     */
    boolean execute(Transaction transaction);
}
