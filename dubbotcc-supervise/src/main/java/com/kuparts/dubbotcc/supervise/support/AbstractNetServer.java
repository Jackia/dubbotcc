package com.kuparts.dubbotcc.supervise.support;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.commons.exception.TccException;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.supervise.EventType;
import com.kuparts.dubbotcc.supervise.TChannel;
import com.kuparts.dubbotcc.supervise.TChannelEventListener;
import com.kuparts.dubbotcc.supervise.TNetEvent;
import com.kuparts.dubbotcc.supervise.net.NetHelper;
import com.kuparts.dubbotcc.supervise.net.NetServer;
import com.kuparts.dubbotcc.supervise.propety.Actor;
import com.kuparts.dubbotcc.supervise.propety.Context;
import com.kuparts.dubbotcc.supervise.propety.InvokeCommand;
import com.kuparts.dubbotcc.supervise.propety.NotifyClient;

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
        System.out.println("收到命令了............" + channel.remoteAddress() + ",command:" + command);
    }

    @Override
    public boolean notify(Actor targetAcotr, InvokeCommand command, NotifyCallback... callbacks) {

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
            LOG.error("init server lister error......." + e.getMessage());
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
            excute();
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
