package com.kuparts.dubbotcc.core.dispatch.zookeeper;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.remoting.zookeeper.ChildListener;
import com.alibaba.dubbo.remoting.zookeeper.ZookeeperClient;
import com.alibaba.dubbo.remoting.zookeeper.ZookeeperTransporter;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.core.dispatch.propety.Actor;
import com.kuparts.dubbotcc.core.dispatch.propety.ActorType;
import com.kuparts.dubbotcc.core.dispatch.propety.Context;
import com.kuparts.dubbotcc.core.dispatch.propety.InvokeCommand;
import com.kuparts.dubbotcc.core.dispatch.support.AbstractSupervise;
import com.kuparts.dubbotcc.core.dispatch.support.Mediator;
import com.kuparts.dubbotcc.core.dispatch.support.NotifyCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * 通过zookeeper管理事务参与者
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/

public class ZookeeperSupervise extends AbstractSupervise {

    protected static final Logger LOG = LoggerFactory.getLogger(ZookeeperSupervise.class);
    private final ZookeeperClient client;

    public ZookeeperSupervise(Mediator mediator) {
        super(mediator);
        ZookeeperTransporter zt = ExtensionLoader.getExtensionLoader(ZookeeperTransporter.class).getAdaptiveExtension();
        client = zt.connect(registerURL);
    }

    @Override
    public boolean notice(String namespace, String url) {
        Assert.notNull(namespace);
        Assert.notNull(url);
        String zurl = namespace + "/" + URL.encode(url);
        client.create(zurl, true);//临时节点
        List<String> urls = client.addChildListener(namespace, new ChildListener() {
            @Override
            public void childChanged(String path, List<String> children) {
                LOG.debug("zookeeper childeren change...." + namespace);
                if (path.equals(Context.getContext().getNamespace())) {
                    master(children);
                }
            }
        });
        client.addStateListener(connected -> System.out.println(connected + ":连接状态................"));
        if (urls == null || urls.isEmpty()) {
            URL objUrl = URL.valueOf(url);
            if (objUrl.getParameter("type").equals(ActorType.OBSERVE.name())) {
                objUrl.removeParameter("type");
                objUrl.addParameter("type", ActorType.MASTER.name());
                zurl = namespace + "/" + URL.encode(objUrl.toFullString());
            }
            client.create(zurl, true);
        }
        List<Actor> exisActor = Actor.parseActor(urls);
        exisActor.forEach(e -> Context.getContext().putIfActor(e));
        listener();//监听
        LOG.debug("start zookeeper childeren listener....");
        return true;
    }


    @Override
    public List<Actor> select(String namespace) {
        if (namespace == null) {
            Collections.emptyList();
        }
        List<String> list = client.getChildren(namespace);
        return Actor.parseActor(list);
    }

    @Override
    public void cancel(String namespace, String url) {

    }

    /**
     * 判断节点变化情况
     *
     * @param urls
     */
    @Override
    public void master(List<String> urls) {
        List<Actor> nodes = Actor.parseActor(urls);
        //得到变化节点
        List<Actor> newNodes = new ArrayList<>();
        Set<Map.Entry<String, Actor>> serviceNodes = Context.getContext().getActors();//得到数据
        Set<Map.Entry<String, Actor>> serviceNodesBack = new HashSet<>(serviceNodes);//得到一个副本
        if (serviceNodes != null && !serviceNodes.isEmpty()) {
            for (Map.Entry<String, Actor> service : serviceNodes) {
                for (Actor node : nodes) {
                    if (Objects.equals(service.getKey(), node.getNumber())
                            || Objects.equals(service.getValue(), node)) {
                        System.out.println("breanK:" + service.getKey() + "," + node.getNumber());
                        serviceNodesBack.remove(service);
                    }
                }
            }
            serviceNodesBack.forEach(e -> newNodes.add(e.getValue()));
        } else {
            newNodes.addAll(nodes);
        }

        CONDITION cond = condition(nodes.size(), newNodes.size());//得到条件
        System.out.println("cond:" + cond.name());
        switch (cond) {
            case MUCH:
                Context.getContext().putIfActor(nodes);
                break;
            case FEW:
                Context.getContext().remove(newNodes);
                break;
            case EQULE:
                Context.getContext().reset(nodes);
                break;
        }
        System.out.println("操作后...:" + Context.getContext().getActorCount());
        ActorType type = null;
        ActorType cucurret = Context.getContext().getCurrentActor().getType();
        //判断自身节点是否为主节点
        if (cucurret == ActorType.MASTER) {
            //如果变化的节点大于当前的节点表示有新增节点,如果新增的是观察节点,则设置当前的master节点为观察节点
            if (cond == CONDITION.MUCH) {
                Optional<Actor> optional = newNodes.stream().filter(e -> e.getType() == ActorType.OBSERVE).findFirst();
                if (optional.isPresent()) {
                    Context.getContext().setMasterActor(optional.get());
                }
            } else if (cond != CONDITION.EQULE) {//表示有节点变化
                //发送命令
                type = ActorType.MASTER;
            }
        } else if (cucurret == ActorType.OBSERVE) {//是否为观察者节点
            if (cond != CONDITION.MUCH) {
                long count = newNodes.stream().filter(e -> e.getType() == ActorType.MASTER).count();
                if (count > 0) {
                    //发送命令
                    type = ActorType.OBSERVE;
                }
            }
        } else {
            if (cond != CONDITION.MUCH) {
                long count = newNodes.stream().filter(e -> e.getType() == ActorType.MASTER && e.getType() == ActorType.OBSERVE).count();
                if (count > 0) {
                    type = ActorType.SLAVE;
                }
            }
        }
        System.out.println("type:" + type);
        if (type != null) {
            //发送命令
            InvokeCommand command = new InvokeCommand();
            command.setSid(Context.getContext().getSid());
            notify(type, command, newNodes);
        }


    }

    /**
     * 校验条件
     *
     * @param size  全量节点
     * @param size2 不同节点
     */
    private CONDITION condition(int size, int size2) {
        int size3 = Context.getContext().getActorCount();
        System.out.println("size1:" + size + ",size2:" + size2 + ",size3:" + size3);
        if (size2 == 0 && size > size3) {
            return CONDITION.MUCH;
        }
        if (size > size3 && size2 > 0) {
            return CONDITION.MUCH;
        }
        if (size < size3 && size2 > 0) {
            return CONDITION.FEW;
        }
        if (size == size3 && size2 == 0) {
            return CONDITION.EQULE;
        }
        return CONDITION.NONE;
    }

    /**
     * 服和监听
     */
    private void listener() {
        if (client != null && client.isConnected()) {
            async();
        }
    }

    @Override
    public boolean notify(Actor targetActor, InvokeCommand command, NotifyCallback... callbacks) {

        return true;
    }

    /**
     * ZOOKEEPER变化状态
     */
    enum CONDITION {
        MUCH,
        FEW,
        EQULE,
        NONE
    }
}
