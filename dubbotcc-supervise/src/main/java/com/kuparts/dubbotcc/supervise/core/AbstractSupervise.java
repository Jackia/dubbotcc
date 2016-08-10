package com.kuparts.dubbotcc.supervise.core;

import com.alibaba.dubbo.common.URL;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.supervise.propety.Actor;

import java.util.List;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public abstract class AbstractSupervise implements Supervise {
    protected URL url;

    /**
     * 注册一个URL
     *
     * @param url 根据actor生成的url
     */
    public abstract void notice(String namespace, String url);

    @Override
    public void notice(Actor actor) {
        Assert.notNull(actor);
        String space = actor.nameSpace();
        String url = actor.getUrlStr();
        this.notice(space, url);
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
