package com.kuparts.dubbotcc.core.dispatch.support;

import com.kuparts.dubbotcc.core.dispatch.propety.Actor;
import com.kuparts.dubbotcc.core.dispatch.propety.InvokeCommand;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public interface Notify {
    /**
     * 通知目标节点
     *
     * @param targetActor 目标节点
     * @param command     命令
     * @return 通知是否成功
     */
    boolean notify(Actor targetActor, InvokeCommand command, NotifyCallback... callbacks);

}
