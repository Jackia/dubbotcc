package com.kuparts.dubbotcc.core.dispatch.command;

import com.kuparts.dubbotcc.core.dispatch.TChannel;
import com.kuparts.dubbotcc.core.dispatch.Command;
import com.kuparts.dubbotcc.core.dispatch.propety.Actor;
import com.kuparts.dubbotcc.core.dispatch.propety.CommandType;

import java.util.List;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@CommandType.CommandTask(CommandType.SLAVE_OBSERVE)
public class CommnadSlaveToObserve implements Command {

    @Override
    public boolean receive(TChannel channel, List<Actor> actors) throws Exception {
        System.out.println("任务执行成功......." + actors);
        return true;
    }
}
