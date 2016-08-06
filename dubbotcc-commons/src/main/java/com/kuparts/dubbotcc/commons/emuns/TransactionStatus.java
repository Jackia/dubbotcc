package com.kuparts.dubbotcc.commons.emuns;

/**
 * 事务执行过程中的状态.
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public enum TransactionStatus {
    BEGIN,//开始事务
    COMMIT,//提交事务
    ROLLBACK,//事务回滚
    FAILURE//事务失败
}
