package com.kuparts.dubbotcc.supervise.support;

import com.kuparts.dubbotcc.supervise.propety.Actor;
import com.kuparts.dubbotcc.supervise.propety.InvokeCommand;
import com.kuparts.dubbotcc.supervise.propety.NotifyClient;

/**
 * 命令交互
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public interface Mediator {
    void execute(NotifyClient client, Actor actor, InvokeCommand command, NotifyCallback... callback);

    /**
     * 注册一个命令
     */
    void register(NotifyClient client, Notify notify);
}
