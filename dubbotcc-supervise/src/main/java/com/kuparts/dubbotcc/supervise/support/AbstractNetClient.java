package com.kuparts.dubbotcc.supervise.support;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.supervise.*;
import com.kuparts.dubbotcc.supervise.net.NetClient;
import com.kuparts.dubbotcc.supervise.propety.Actor;
import com.kuparts.dubbotcc.supervise.propety.Context;
import com.kuparts.dubbotcc.supervise.propety.InvokeCommand;
import com.kuparts.dubbotcc.supervise.propety.NotifyClient;

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
        future.addListener(new TChannelHandlerListener() {
            @Override
            public void operationComplete(TFuture future) throws Exception {
                if (future.isSuccessfully()) {
                    System.out.println("初始化成功.......");
                }
            }
        });
    }

    @Override
    public boolean notify(Actor targetAcotr, InvokeCommand command, NotifyCallback... callbacks) {
        Assert.notNull(targetAcotr);
        Assert.notNull(command);

        /*if (callbacks == null || callbacks.length < 0) {
            synInvoker();
        } else {
            asynInvoker();
        }*/
        TChannel channel = null;
        boolean flag = true;
        try {
            channel = connection(targetAcotr.getLocal(), targetAcotr.getPort());
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
        startClient();
        /*try {
           *//* InetAddress address = InetAddress.getByAddress(context.getMasterActor().getLocal().getBytes());
            connection(address, context.getMasterActor().getPort());*//*
        } catch (UnknownHostException e) {

        }*/
        excute();
    }
}
