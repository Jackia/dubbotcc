package com.kuparts.dubbotcc.core.config;

/**
 * 扩展配置信息
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TccExtConfig extends TccConfig{
    /**
     * 回滚队列大小
     */
    private int rollbackQueueMax;
    /**
     * 监听回滚队列线程数
     */
    private int rollbackThreadMax;

    //------------get and set method ---------


    public int getRollbackQueueMax() {
        return rollbackQueueMax;
    }

    public void setRollbackQueueMax(int rollbackQueueMax) {
        this.rollbackQueueMax = rollbackQueueMax;
    }

    public int getRollbackThreadMax() {
        return rollbackThreadMax;
    }

    public void setRollbackThreadMax(int rollbackThreadMax) {
        this.rollbackThreadMax = rollbackThreadMax;
    }
}
