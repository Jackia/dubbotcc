package com.kp.dubbotcc.core.rollback;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.kp.dubbotcc.api.TccServicePoint;
import com.kp.dubbotcc.api.Transaction;
import com.kp.dubbotcc.commons.emuns.ServicePointStatus;
import com.kp.dubbotcc.commons.utils.Assert;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * 执行缓存命令.
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class RollbackCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(Command.class);

    @Override
    public boolean execute(Transaction transaction) {

        transaction.getPotins().stream().filter(e -> e.getStatus() == ServicePointStatus.SUCCESS).forEach(point -> {
            //回滚执行器
            CompletableFuture.supplyAsync(new Worker(point)).thenAccept(st -> {
                if (!st) {
                    System.out.println("结果失败...");
                }
            });
        });
        return false;
    }

    /**
     * 具体事务回滚器
     */
    class Worker implements Supplier<Boolean> {

        TccServicePoint point;
        private Worker(TccServicePoint point) {
            this.point = point;
        }

        @Override
        public Boolean get() {
            LOG.info("start executing  rollback transaction" + point.getTransId() + "rollback:" + point.getRollbackInvocation().getMethod());
            Assert.notNull(point);
            ReferenceConfig reference = new ReferenceConfig();
            return Boolean.TRUE;
        }
    }
}
