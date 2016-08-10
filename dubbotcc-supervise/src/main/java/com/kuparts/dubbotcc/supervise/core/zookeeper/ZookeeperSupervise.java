package com.kuparts.dubbotcc.supervise.core.zookeeper;

import com.kuparts.dubbotcc.commons.config.TccExtConfig;
import com.kuparts.dubbotcc.supervise.core.AbstractSupervise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 通过zookeeper管理事务参与者
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@Component
public class ZookeeperSupervise extends AbstractSupervise {


    @Autowired
    TccExtConfig config;

    /*public ZookeeperSupervise() {

        System.out.println("111111111111111111");
        ZookeeperTransporter zt = ExtensionLoader.getExtensionLoader(ZookeeperTransporter.class).getAdaptiveExtension();
        System.out.println(config.getZookurl());
    }*/

    @PostConstruct
    public void init() {
        System.out.println("ddddddddddddddddddddddddddddddddd");
    }

    @Override
    public void notice(String namespace, String url) {

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
