package com.kp.dubbotcc.core.rollback;

import com.kp.dubbotcc.api.Transaction;
import com.kp.dubbotcc.commons.utils.Assert;

/**
 * 传一个执行事务动作
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TransmitAction {

    private final Action action;

    public TransmitAction(Action action) {
        this.action = action;
    }

    /**
     * 开始动作
     *
     * @return 是否执行成功
     */
    public boolean action(Transaction transaction) {
        Assert.notNull(transaction);
        Assert.notNull(transaction.getPotins());
        return action.execute(transaction);
    }
}
