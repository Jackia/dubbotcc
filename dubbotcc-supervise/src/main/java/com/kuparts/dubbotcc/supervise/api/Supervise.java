package com.kuparts.dubbotcc.supervise.api;

import com.kuparts.dubbotcc.supervise.propety.Actor;

import java.util.List;

/**
 * 管理者具体接口定义
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public interface Supervise {
    /**
     * 通知管理者
     *
     */
    void notice();
    /**
     * 根据一个namespace获取下面注册的所有地址
     *
     * @param namespace namespace
     * @return 列表
     */
     List<Actor> select(String namespace);
}
