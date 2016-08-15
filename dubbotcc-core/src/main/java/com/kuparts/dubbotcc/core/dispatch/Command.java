package com.kuparts.dubbotcc.core.dispatch;

import com.kuparts.dubbotcc.core.dispatch.TChannel;
import com.kuparts.dubbotcc.core.dispatch.propety.Actor;

import java.util.List;

/**
 * 事务调度后,接收后的执行..
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public interface Command {
    /**
     * 命令执行器
     *
     * @param channel 当前调用通道
     * @param actors  接管事务,需要接管的事务
     * @return 命令执行结果
     */
    boolean receive(TChannel channel, List<Actor> actors) throws Exception;

}
