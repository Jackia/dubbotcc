package com.kp.dubbotcc.core.rollback;

import com.kp.dubbotcc.api.Transaction;

/**
 * 清除已经缓存的事务对象
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class ClearCacheAction implements Action {
    @Override
    public boolean execute(Transaction transaction) {
        return false;
    }
}
