package com.kuparts.dubbotcc.supervise.core;

import com.kuparts.dubbotcc.supervise.propety.Actor;

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
     * @param actor 管理者信息
     */
    void notice(Actor actor);
}
