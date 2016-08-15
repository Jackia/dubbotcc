package com.kuparts.dubbotcc.core.dispatch.support;

import com.kuparts.dubbotcc.core.dispatch.propety.Actor;
import com.kuparts.dubbotcc.core.dispatch.propety.InvokeCommand;
import com.kuparts.dubbotcc.core.dispatch.propety.NotifyClient;

/**
 * 交互
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public interface Mediator {
    /**
     * 请求调用
     *
     * @param client   客户端标识
     * @param actor    调用目标
     * @param command  执行指令
     * @param callback 调用后的回调
     */
    void execute(NotifyClient client, Actor actor, InvokeCommand command, NotifyCallback... callback);

    /**
     * 注册需要回调的客户端
     *
     * @param client 客户端唯一标识
     * @param notify 回调者
     */
    void register(NotifyClient client, Notify notify);
}
