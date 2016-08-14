package com.kuparts.dubbotcc.supervise.support;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.commons.exception.TccRuntimeException;
import com.kuparts.dubbotcc.supervise.api.Supervise;
import com.kuparts.dubbotcc.supervise.propety.Actor;
import com.kuparts.dubbotcc.supervise.propety.Context;
import com.kuparts.dubbotcc.supervise.propety.InvokeCommand;
import com.kuparts.dubbotcc.supervise.propety.NotifyClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    protected URL rigisterURL;

    Mediator mediator;

    protected AbstractSupervise(Mediator mediator) {
        rigisterURL = Context.getContext().getRigisterUrl();
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
            throw new TccRuntimeException("start dubbotcc error..............");
        }
    }

    /**
     * 异步监听
     */
    protected void async() {
        CompletableFuture.runAsync(() -> {

            if (rigisterURL != null) {
                synchronized (rigisterURL) {
                    try {
                        wait();//等待
                    } catch (InterruptedException e) {

                    } finally {

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
     * 节点变化,操作
     */
    public abstract void master(List<String> urls);

    @Override
    protected boolean execut(Actor targetAcotr, InvokeCommand command, NotifyCallback... callbacks) {
        if (targetAcotr == null) {
            return false;
        }
        if (command == null) {
            return false;
        }
        System.out.println("notify:" + targetAcotr);
        try {
            mediator.execute(NotifyClient.NETCLIENT, targetAcotr, command, callbacks);
        } catch (RuntimeException ex) {
            LOG.info("notify error : " + targetAcotr);
            return false;
        }
        return true;
    }
}
