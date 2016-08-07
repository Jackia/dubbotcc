package com.kuparts.dubbotcc.core.rollback;


import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.api.CompensationCallback;
import com.kuparts.dubbotcc.api.TccResponse;
import com.kuparts.dubbotcc.api.Transaction;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.core.spring.TcccMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
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
    protected static final Map<String, CallbackType> CALLBACKS = new ConcurrentHashMap<>();

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
     * bean方法填充
     *
     * @param callbacks 回调方法集合
     */
    public void fullCallback(Map<String, Object> callbacks) {
        callbacks.forEach((k, v) -> {
            Method[] methods = v.getClass().getMethods();
            Assert.notNull(methods);
            Arrays.stream(methods).filter(m -> m.getAnnotation(TcccMethod.class) != null &&
                    m.getParameterTypes().length == 1 &&
                    m.getParameterTypes()[0].getName().equals(TccResponse.class.getName()))
                    .forEach(m -> {
                        CallbackType t = new CallbackType();
                        t.setMethod(m);
                        t.setCallClazz(v.getClass());
                        t.setMark(Mark.TCCC);
                        callbacks.put(k, t);
                    });
        });
    }

    /**
     * 实现接口填充
     *
     * @param list 配置文件设置
     */
    public void fullCallback(List<CompensationCallback> list) {
        list.forEach(e -> {
            CallbackType t = new CallbackType();
            t.setCallClazz(e.getClass());
            t.setMark(Mark.API);
            try {
                t.setMethod(e.getClass().getMethod("callback", TccResponse.class));
                CALLBACKS.put(e.getClass().getName(), t);
            } catch (NoSuchMethodException e1) {
                LOG.info(e1.getMessage());
            }

        });
    }

    public void submit(Transaction transaction) {
        try {
            QUEUE.put(transaction);
        } catch (InterruptedException e) {
            LOG.info("需要回滚的事务提交到队列失败..");
        }
    }
}
