package com.kuparts.dubbotcc.supervise.support;

import com.kuparts.dubbotcc.supervise.propety.Actor;
import com.kuparts.dubbotcc.supervise.propety.InvokeCommand;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public interface Notify {
    /**
     * 通知目标节点
     *
     * @param targetAcotr 目标节点
     * @param command     命令
     * @return 通知是否成功
     */
    boolean notify(Actor targetAcotr, InvokeCommand command, NotifyCallback... callbacks);

}
