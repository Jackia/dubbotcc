package com.kuparts.dubbotcc.core.dispatch.support;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.commons.config.TccExtConfigConstants;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.core.dispatch.TChannel;
import com.kuparts.dubbotcc.core.dispatch.TChannelEventListener;
import com.kuparts.dubbotcc.core.dispatch.Supervise;
import com.kuparts.dubbotcc.core.dispatch.propety.Actor;
import com.kuparts.dubbotcc.core.dispatch.propety.ActorStatus;
import com.kuparts.dubbotcc.core.dispatch.propety.ActorType;
import com.kuparts.dubbotcc.core.dispatch.propety.Context;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;

/**
 * 自定义事件监听器
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class NetServerEventListener implements TChannelEventListener {

    Supervise supervise;
    protected static final Logger LOG = LoggerFactory.getLogger(NetServerEventListener.class);

    public NetServerEventListener(Supervise supervise) {

        this.supervise = supervise;
    }

    @Override
    public void onConnection(String address, TChannel tChannel) throws Exception {
        Assert.notNull(address);
        Assert.notNull(tChannel);
        //初始化当前节点
        Actor actor = new Actor();
        actor.setLocal(address);
        int port = ((InetSocketAddress) tChannel.localAddress()).getPort();
        actor.setPort(port);
        actor.setName(Context.getContext().getName());
        actor.setStatus(ActorStatus.RUNING);
        //注册
        Actor master = Context.getContext().getMasterActor();
        //如果没有主节点就将当前的节点注册成主节点
        if (master == null && Context.getContext().getActorCount() <= 0) {
            actor.setType(ActorType.MASTER);
            Context.getContext().setMasterActor(actor);
        } else if (master != null && Context.getContext().getActorCount() == 1) {
            actor.setType(ActorType.OBSERVE);
        } else {
            long count = Context.getContext().getActors().stream()
                    .filter(e -> e.getValue().getType() == ActorType.MASTER).count();
            if (count <= 0) {
                actor.setType(ActorType.MASTER);
                Context.getContext().setMasterActor(actor);
            } else if (count > 0) {

                count = Context.getContext().getActors().stream()
                        .filter(e -> e.getValue().getType() == ActorType.OBSERVE).count();
                if (count <= 0) {
                    actor.setType(ActorType.OBSERVE);
                } else {
                    actor.setType(ActorType.SLAVE);
                }
            }
        }
        actor.setNumber(getNumber(actor));
        Context.getContext().setCurrentActor(actor);
        LOG.debug("node :" + actor);
        Context.getContext().putActor(actor);
        //注册服务,如果已经注册不再进行注册
        supervise.notice();
    }

    @Override
    public void onChannelActive(String address, TChannel tChannel) throws Exception {

    }

    @Override
    public void onChannelClose(String address, TChannel tChannel) throws Exception {

    }

    @Override
    public void onChannelReadComplete(String address, TChannel tChannel) throws Exception {

    }

    @Override
    public void onExceptionCaught(String address, TChannel tChannel) throws Exception {

    }

    @Override
    public void onChannelIdle(String address, TChannel tChannel) throws Exception {

    }

    /**
     * 生成统一号
     *
     * @param actor
     * @return
     */
    private int getNumber(Actor actor) {
        String myid = Context.getContext().getProperties(TccExtConfigConstants.MY_ID);
        if (StringUtils.isBlank(myid)) {
            int b = actor.getLocal()
                    .concat(String.valueOf(actor.getPort()))
                    .concat(actor.getName())
                    .concat(String.valueOf(actor.getRunTime())).hashCode();
            b = b < 0 ? (b * -1) : b;
            myid = String.valueOf(b);
            Context.getContext().putPropertis(TccExtConfigConstants.MY_ID, myid);
        }
        return Integer.parseInt(myid);
    }
}
