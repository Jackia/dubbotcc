package com.kp.dubbotcc.core.rollback;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kp.dubbotcc.api.Transaction;
import com.kp.dubbotcc.commons.emuns.ServicePointStatus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 执行回滚操作
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class RollBackService extends RollbackQueue {
    private static final Logger LOG = LoggerFactory.getLogger(RollBackService.class);
    /**
     * 线程池大小
     */
    private final int MAX_THREAD = Runtime.getRuntime().availableProcessors() * 2;

    private final ExecutorService service = Executors.newFixedThreadPool(MAX_THREAD);

    /**
     * 监听回滚队列
     */
    public void listerQueue() {
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
                Transaction transaction = queue.take();//得到需要回滚的事务对象
                if (transaction.getPotins().size() > 0) {

                }
            } catch (InterruptedException e) {

            }
            execute();
        }
    }
}
