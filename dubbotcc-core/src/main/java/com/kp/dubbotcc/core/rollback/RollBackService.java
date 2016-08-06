package com.kp.dubbotcc.core.rollback;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kp.dubbotcc.api.Transaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 执行回滚操作
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class RollbackService extends RollbackQueue {
    private static final Logger LOG = LoggerFactory.getLogger(RollbackService.class);
    /**
     * 线程池大小
     */
    private final int MAX_THREAD = Runtime.getRuntime().availableProcessors() * 2;

    private final ExecutorService service = Executors.newFixedThreadPool(MAX_THREAD);


    /**
     * 监听回滚队列
     */
    public void listerQueue() {
        LOG.info(" start rollback transaction queue Listening ..............max:" + MAX_THREAD);
        for (int i = 0; i < MAX_THREAD; i++) {
            service.execute(new Worker());
        }
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
            try {
                while (true) {
                    Transaction transaction = queue.take();//得到需要回滚的事务对象
                    if (transaction != null) {
                        boolean flag = new TransmitAction(new RollbackAction()).action(transaction);
                    }
                }
            } catch (Exception e) {
                LOG.error("rollback transaction failure ," + e.getMessage());
            }
        }
    }
}