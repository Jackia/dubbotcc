package com.kuparts.dubbotcc.supervise.support;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.commons.exception.TccRuntimeException;
import com.kuparts.dubbotcc.supervise.api.Supervise;
import com.kuparts.dubbotcc.supervise.propety.Actor;
import com.kuparts.dubbotcc.supervise.propety.Context;

import java.util.List;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public abstract class AbstractSupervise implements Supervise {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSupervise.class);
    /**
     * 生成服务属性地址
     */
    protected URL actorUrl;
    /**
     * 注册的服务器址
     */
    protected URL rigisterURL;

    public AbstractSupervise() {
        rigisterURL = Context.getContext().getRigisterUrl();
    }

    /**
     * 注册一个URL
     *
     * @param url 根据actor生成的url
     */
    public abstract boolean notice(String namespace, String url);

    @Override
    public void notice() {
        Actor actor = Context.getContext().getCurrentActor();
        String space = actor.nameSpace();
        String url = actor.getURL().toFullString();
        actorUrl = actor.getURL();
        boolean flag = this.notice(space, url);
        if (!flag) {
            LOG.error("start dubbotcc error");
            new TccRuntimeException("start dubbotcc error..............");
        }
    }

    /**
     * 根据一个namespace获取下面注册的所有地址
     *
     * @param namespace namespace
     * @return 列表
     */
    public abstract List<String> select(String namespace);

    /**
     * 取消一个本地服务
     *
     * @param namespace
     * @param url
     */
    public abstract void cancel(String namespace, String url);

    /**
     * 主节点操作
     */
    public abstract void mester();

}
