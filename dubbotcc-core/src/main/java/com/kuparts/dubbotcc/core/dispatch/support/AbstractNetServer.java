package com.kuparts.dubbotcc.core.dispatch.support;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.google.common.util.concurrent.FutureCallback;
import com.kuparts.dubbotcc.commons.exception.TccException;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.core.dispatch.TChannel;
import com.kuparts.dubbotcc.core.dispatch.TChannelEventListener;
import com.kuparts.dubbotcc.core.dispatch.net.NetHelper;
import com.kuparts.dubbotcc.core.dispatch.net.NetServer;
import com.kuparts.dubbotcc.core.dispatch.propety.Actor;
import com.kuparts.dubbotcc.core.dispatch.propety.Context;
import com.kuparts.dubbotcc.core.dispatch.propety.EventType;
import com.kuparts.dubbotcc.core.dispatch.propety.InvokeCommand;
import com.kuparts.dubbotcc.core.dispatch.propety.NotifyClient;
import com.kuparts.dubbotcc.core.dispatch.propety.TNetEvent;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * 服务中间层处理
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public abstract class AbstractNetServer extends AbstractNet implements NetServer {
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractNetServer.class);
    //用于注册的服务地址
    protected volatile String localAddress;

    //初始化网络服务
    public AbstractNetServer(Context context, TChannelEventListener listener, Mediator mediator) {
        super(context, listener, mediator);
        //初始化网络IP地址
        localAddress = NetHelper.getlocalAddress().stream().findFirst().get();
        mediator.register(NotifyClient.NETSERVER, this);
    }

    @Override
    protected void processReleaseCommand(TChannel channel, InvokeCommand command) {
        if (channel == null) return;
        if (command == null) return;
        if (command.getTargetActor() == null) return;
        //根据command执行命令
        proxy(channel, command, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    System.out.println("调用成功.....");
                } else {

                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * 其它业务通知处理
     *
     * @param targetActor 目标节点
     * @param command     命令
     * @param callbacks
     * @return
     */
    @Override
    public boolean notify(Actor targetActor, InvokeCommand command, NotifyCallback... callbacks) {

        return false;
    }

    /**
     * 初始化服务
     *
     * @param host IP
     * @param port 端口号
     */
    protected void initServerLister(String host, int port, TChannel channel) {
        if (context == null) {
            LOG.error("context is null");
        }
        Assert.checkConditionArgument(port > 0, "port is error");
        Assert.checkConditionArgument(localAddress.equals(host), "port is error");
        InetSocketAddress inetAddress = new InetSocketAddress(host, port);
        context.setLocalAddress(inetAddress);
        try {
            syncEvent(new TNetEvent(EventType.INIT, localAddress, channel));//同步服务
        } catch (TccException e) {
            LOG.error("onConnection server lister error......." + e.getMessage());
            System.exit(1);//非正常退出
        }
    }

    /**
     * 启动服务
     *
     * @throws Exception
     */
    protected abstract void startServer() throws Exception;


    @Override
    public void start() {
        Assert.notNull(context);
        Assert.notNull(listener);
        try {
            CompletableFuture.runAsync(new NetServerWorker());
            execute();
        } catch (Exception e) {
            LOG.error("start dubbotcc error system exit");
            isRun = false;
            stopServer();//停止线程
            System.exit(1);
        }
    }

    class NetServerWorker implements Runnable {
        @Override
        public void run() {
            try {
                startServer();
            } catch (Exception e) {
                LOG.error("start server error..." + e.getMessage());
                System.exit(1);
            }
        }
    }
}
