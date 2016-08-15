package com.kuparts.dubbotcc.core.dispatch.support;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.core.dispatch.TChannel;
import com.kuparts.dubbotcc.core.dispatch.TChannelEventListener;
import com.kuparts.dubbotcc.core.dispatch.TChannelHandler;
import com.kuparts.dubbotcc.core.dispatch.net.NetClient;
import com.kuparts.dubbotcc.core.dispatch.propety.Actor;
import com.kuparts.dubbotcc.core.dispatch.propety.Context;
import com.kuparts.dubbotcc.core.dispatch.propety.InvokeCommand;
import com.kuparts.dubbotcc.core.dispatch.propety.NotifyClient;

/**
 * 服务客户端中间层处理
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public abstract class AbstractNetClient extends AbstractNet implements NetClient {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractNetClient.class);

    protected AbstractNetClient(Context context, TChannelEventListener listener, Mediator mediator) {
        super(context, listener, mediator);
        mediator.register(NotifyClient.NETCLIENT, this);
    }

    @Override
    protected void processReleaseCommand(TChannel channel, InvokeCommand command) throws Exception {
        TChannelHandler future = channel.writeAndFlush(command);
        future.addListener(future1 -> {
            if (future1.isSuccessfully()) {
                System.out.println("初始化成功.......");
            }
        });
    }

    /**
     * 通知
     *
     * @param targetActor 目标节点
     * @param command     命令
     * @param callbacks
     * @return
     */
    @Override
    public boolean notify(Actor targetActor, InvokeCommand command, NotifyCallback... callbacks) {
        Assert.notNull(targetActor);
        Assert.notNull(command);
        TChannel channel = null;
        boolean flag = true;
        try {
            channel = connection(targetActor.getLocal(), targetActor.getPort());
            processReleaseCommand(channel, command);
        } catch (Exception e) {
            flag = false;
        } finally {
            if (channel != null && !channel.isClose()) {
            }
        }
        return flag;
    }

    /**
     * 关闭通道
     *
     * @param channel 关闭通道
     */
    protected void closeChannel(TChannel channel) {

    }

    /**
     * 初始化客户端
     */
    protected abstract void startClient();

    /**
     * 连接
     */
    protected abstract TChannel connection(String host, int port);

    @Override
    public void start() {
        startClient();//初始化客户端
        execute();//执行队列监听
    }
}
