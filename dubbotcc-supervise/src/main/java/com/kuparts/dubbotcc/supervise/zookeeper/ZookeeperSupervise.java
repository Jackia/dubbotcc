package com.kuparts.dubbotcc.supervise.zookeeper;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.remoting.zookeeper.ZookeeperClient;
import com.alibaba.dubbo.remoting.zookeeper.ZookeeperTransporter;
import com.kuparts.dubbotcc.supervise.support.AbstractSupervise;

import java.util.List;

/**
 * 通过zookeeper管理事务参与者
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/

public class ZookeeperSupervise extends AbstractSupervise {

    private ZookeeperClient client;

    public ZookeeperSupervise() {
        super();
        ZookeeperTransporter zt = ExtensionLoader.getExtensionLoader(ZookeeperTransporter.class).getAdaptiveExtension();
        this.client = zt.connect(rigisterURL);
    }

    @Override
    public boolean notice(String namespace, String url) {
        return true;
    }

    @Override
    public void mester() {

    }

    @Override
    public List<String> select(String namespace) {
        return null;
    }

    @Override
    public void cancel(String namespace, String url) {

    }
}
