package com.kuparts.dubbotcc.core.dispatch.support;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.commons.exception.TccRuntimeException;
import com.kuparts.dubbotcc.core.dispatch.Supervise;
import com.kuparts.dubbotcc.core.dispatch.propety.Actor;
import com.kuparts.dubbotcc.core.dispatch.propety.Context;
import com.kuparts.dubbotcc.core.dispatch.propety.InvokeCommand;
import com.kuparts.dubbotcc.core.dispatch.propety.NotifyClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public abstract class AbstractSupervise extends NotifyListener implements Supervise {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSupervise.class);
    /**
     * 生成服务属性地址
     */
    protected URL actorUrl;
    /**
     * 注册的服务器址
     */
    protected URL registerURL;

    CountDownLatch latch = new CountDownLatch(1);
    Mediator mediator;

    protected AbstractSupervise(Mediator mediator) {
        registerURL = Context.getContext().getRigisterUrl();
        this.mediator = mediator;
        mediator.register(NotifyClient.ZOOKEETPER, this);
    }

    /**
     * 注册一个URL
     *
     * @param url 根据actor生成的url
     */
    public abstract boolean notice(String namespace, String url);

    //异步不结束
    @Override
    public void notice() {
        Actor actor = Context.getContext().getCurrentActor();
        String space = Context.getContext().getNamespace();
        String url = actor.getURL().toFullString();
        actorUrl = actor.getURL();
        boolean flag = notice(space, url);
        if (!flag) {
            LOG.error("start dubbotcc error");
            throw new TccRuntimeException("start dubbotcc error...");
        }
    }

    /**
     * 异步监听
     */
    protected void async() {
        CompletableFuture.runAsync(() -> {

            if (registerURL != null) {
                synchronized (registerURL) {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        latch.countDown();
                    }
                }
            }
        });
    }

    /**
     * 取消一个本地服务
     *
     * @param namespace
     * @param url
     */
    public abstract void cancel(String namespace, String url);

    /**
     * 选择变化节点,同时判断不同的命令.
     *
     * @param urls 全量urls
     */
    public abstract void master(List<String> urls);

    @Override
    protected boolean execute(Actor targetActor, InvokeCommand command, NotifyCallback... callbacks) {
        if (targetActor == null) {
            return false;
        }
        if (command == null) {
            return false;
        }
        System.out.println("notify:" + targetActor);
        try {
            mediator.execute(NotifyClient.NETCLIENT, targetActor, command, callbacks);
        } catch (RuntimeException ex) {
            LOG.info("notify error : " + targetActor);
            return false;
        }
        return true;
    }
}
