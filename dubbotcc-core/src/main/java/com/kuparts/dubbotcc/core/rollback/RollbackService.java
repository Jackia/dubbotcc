package com.kuparts.dubbotcc.core.rollback;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.api.Callback;
import com.kuparts.dubbotcc.api.Transaction;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.core.config.TccExtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 执行回滚操作
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@Component
public class RollbackService {
    private static final Logger LOG = LoggerFactory.getLogger(RollbackService.class);
    /**
     * 线程池大小
     */
    private int MAX_THREAD;

    private ExecutorService service;

    @Autowired
    private CallbackService callbackService;

    /**
     * 监听回滚队列
     */
    public void listerQueue() {
        synchronized (LOG) {
            QUEUE = new LinkedBlockingQueue<>(confg.getRollbackQueueMax());
            MAX_THREAD = confg.getRollbackQueueMax();
            service = Executors.newFixedThreadPool(MAX_THREAD);
            LOG.info(" start rollback transaction QUEUE Listening ..............max:" + MAX_THREAD);
            for (int i = 0; i < MAX_THREAD; i++) {
                service.execute(new Worker());
            }
        }
    }

    @Autowired
    protected TccExtConfig confg;
    /**
     * 需要回滚的事务队列
     */
    protected BlockingQueue<Transaction> QUEUE;

    /**
     * 初始化失败的队列信息
     * 注意:这里初始化指的是已经持久化的事务信息:
     * 暂时不实现
     */
    protected static void initQueue() {
        //初始化缓存没有里面没有回滚的事务
        //TODO 初始化持久化未回滚的事务未实现
    }


    /**
     * 提交回滚事务
     *
     * @param transaction 事务对象
     * @param elements    事务的调用Stack信息
     */
    public void submit(Transaction transaction, StackTraceElement[] elements) {
        Assert.notNull(transaction);
        //查找当前的Stack信息
        CompletableFuture.supplyAsync(() -> {
            Callback target = Arrays.stream(elements).filter(e -> callbackService.existCallback(e))
                    .flatMap(stackTraceElement -> callbackService.stackCallback(stackTraceElement).stream())
                    .findFirst().get();
            transaction.setCallback(target);
            return transaction;
        }).thenAccept(e -> {
            try {
                if (e != null)
                    QUEUE.put(e);
                LOG.info("asyn submit transaction to queue ......");
            } catch (InterruptedException e1) {
                LOG.info("需要回滚的事务提交到队列失败..");
            }
        });
    }
    /**
     * 线程执行器
     */
    class Worker implements Runnable {

        @Override
        public void run() {
            execute();
        }

        /**
         * 事务执行..
         */
        private void execute() {
            Transaction transaction;
            while (true) {
                try {
                    transaction = QUEUE.take();//得到需要回滚的事务对象
                    if (transaction != null) {
                        new TransmitAction(new RollbackAction()).action(transaction);
                    }
                } catch (Exception e) {
                    LOG.error("rollback transaction failure ," + e.getMessage());
                }
            }

        }
    }
}
