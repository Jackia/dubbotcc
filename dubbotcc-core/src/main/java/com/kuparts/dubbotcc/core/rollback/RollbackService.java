package com.kuparts.dubbotcc.core.rollback;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.api.Transaction;
import com.kuparts.dubbotcc.core.config.TccConfig;
import com.kuparts.dubbotcc.core.rollback.task.ClearCacheCallback;
import com.kuparts.dubbotcc.core.spring.BeanUtils;

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
    private final int MAX_THREAD = Runtime.getRuntime().availableProcessors() << 1;

    private final ExecutorService service = Executors.newFixedThreadPool(MAX_THREAD);


    /**
     * 监听回滚队列
     */
    public void listerQueue() {
        LOG.info(" start rollback transaction QUEUE Listening ..............max:" + MAX_THREAD);
        for (int i = 0; i < MAX_THREAD; i++) {
            service.execute(new Worker());
        }
        //初始化回滚后的回调集合..
        //合并集合
        TccConfig config = BeanUtils.getInstance().getBean(TccConfig.class);
        if (config != null && config.getCallbacks() != null) {
            config.getCallbacks().forEach(e -> CALLBACKS.add(e));
        }
        CALLBACKS.add(new ClearCacheCallback());
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
