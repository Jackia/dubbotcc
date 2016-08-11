package com.kuparts.dubbotcc.supervise.zookeeper;

import com.kuparts.dubbotcc.supervise.api.Supervise;
import com.kuparts.dubbotcc.supervise.api.SuperviseFactory;

/**
 * 获取一个监听者
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class ZookeeperSuperviseFactory implements SuperviseFactory {
    @Override
    public Supervise supervise() {
        return new ZookeeperSupervise();
    }
}
