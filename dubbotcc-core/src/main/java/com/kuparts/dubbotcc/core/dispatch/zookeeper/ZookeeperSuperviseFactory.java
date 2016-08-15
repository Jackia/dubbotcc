package com.kuparts.dubbotcc.core.dispatch.zookeeper;

import com.kuparts.dubbotcc.core.dispatch.Supervise;
import com.kuparts.dubbotcc.core.dispatch.SuperviseFactory;
import com.kuparts.dubbotcc.core.dispatch.support.Mediator;

/**
 * 获取一个监听者
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class ZookeeperSuperviseFactory implements SuperviseFactory {
    private Mediator mediator;

    public ZookeeperSuperviseFactory(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public Supervise supervise() {
        return new ZookeeperSupervise(mediator);
    }
}
