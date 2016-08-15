package com.kuparts.dubbotcc.core.dispatch.support;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.google.common.base.Joiner;
import com.kuparts.dubbotcc.commons.api.SuperviseService;
import com.kuparts.dubbotcc.commons.bean.BeanServiceUtils;
import com.kuparts.dubbotcc.commons.config.TccExtConfig;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.core.dispatch.Supervise;
import com.kuparts.dubbotcc.core.dispatch.net.NetClient;
import com.kuparts.dubbotcc.core.dispatch.net.NetServer;
import com.kuparts.dubbotcc.core.dispatch.net.netty.NettyNetClient;
import com.kuparts.dubbotcc.core.dispatch.net.netty.NettyNetServer;
import com.kuparts.dubbotcc.core.dispatch.propety.Actor;
import com.kuparts.dubbotcc.core.dispatch.propety.ActorStatus;
import com.kuparts.dubbotcc.core.dispatch.propety.ActorType;
import com.kuparts.dubbotcc.core.dispatch.propety.Context;
import com.kuparts.dubbotcc.core.dispatch.zookeeper.ZookeeperSuperviseFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 负责启动监管系统,对各子系统的事务管理,
 * 自动接管,上线注册待
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@Component
@Scope("prototype")
public class ParentSuperviseService implements SuperviseService {

    private TccExtConfig config;

    private volatile boolean isRunning = false;

    NetClient client;

    Mediator mediator = new NotifyMediator();

    Supervise supervise;

    public ParentSuperviseService() {
        config = BeanServiceUtils.getInstance().getBean(TccExtConfig.class);
    }

    public ParentSuperviseService(TccExtConfig config) {
        this.config = config;
    }

    protected static final Logger LOG = LoggerFactory.getLogger(ParentSuperviseService.class);

    /**
     * 启动事务监听
     */
    @Override
    public void start() {
        Assert.notNull(config);
        if (!isRunning) {
            //初始化Context初始
            initContext();
            //开始运行服务
            NetServer server = new NettyNetServer(Context.getContext(), new NetServerEventListener(supervise), mediator);
            server.start();
            client = new NettyNetClient(Context.getContext(), null, mediator);
            client.start();
            isRunning = true;
        }
    }

    /**
     * 本机测试
     */
    public void test(String num) {
        Context.getContext().putPropertis("myid", num);
        start();
    }

    /**
     * 设置一些常用数据,
     * 有部分数据可能需要网络服务初始人成功后,再加载
     */
    private void initContext() {
        //初始化Context信息
        URL zookUrl = URL.valueOf(config.getZookurl());
        Context.getContext().setRigisterUrl(zookUrl);
        Context.getContext().setSid(config.getSerializer());
        Context.getContext().setName(config.getApplication());
        //获取当前namespace下面的所有节点
        ZookeeperSuperviseFactory factory = new ZookeeperSuperviseFactory(mediator);
        supervise = factory.supervise();
        String namespace = getNameSpace();
        Context.getContext().setNamespace(namespace);
        List<Actor> list = supervise.select(namespace);
        //加入所有目录下的节点数据
        if (list != null) {
            list.forEach(e -> {
                //设置主节点
                if (e.getType() == ActorType.MASTER && e.getStatus() == ActorStatus.RUNING) {
                    Context.getContext().setMasterActor(e);
                    LOG.debug("load master node :" + e);
                }
                LOG.debug("load node :" + e);
                Context.getContext().putActor(e);
            });
        }
    }

    /**
     * 获取当前系统的主要namespace
     *
     * @return namespace
     */
    public String getNameSpace() {
        return Joiner.on("/").skipNulls().join(new String[]{"", "dubbotcc", config.getApplication()});
    }
}
