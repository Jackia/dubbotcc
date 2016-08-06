package com.kp.dubbotcc.core.rollback;


import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kp.dubbotcc.api.CompensationCallback;
import com.kp.dubbotcc.api.Transaction;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 回滚的事务队列..
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class RollbackQueue {
    private static final Logger LOG = LoggerFactory.getLogger(RollbackQueue.class);
    /**
     * 需要回滚的事务队列
     */
    protected static final BlockingQueue<Transaction> QUEUE = new LinkedBlockingQueue<>(5000);

    /**
     * 回滚后的回调操作..
     */
    protected static final List<CompensationCallback> CALLBACKS = new CopyOnWriteArrayList<>();

    /**
     * 初始化失败的队列信息
     * 注意:这里初始化指的是已经持久化的事务信息:
     * 暂时不实现
     */
    protected static void initQueue() {
        //初始化缓存没有里面没有回滚的事务
        //TODO 初始化持久化未回滚的事务未实现
    }

    public void submit(Transaction transaction) {
        try {
            QUEUE.put(transaction);
        } catch (InterruptedException e) {
            LOG.info("需要回滚的事务提交到队列失败..");
        }
    }
}
