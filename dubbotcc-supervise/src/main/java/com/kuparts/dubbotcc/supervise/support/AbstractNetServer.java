package com.kuparts.dubbotcc.supervise.support;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.commons.exception.TccException;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.supervise.TChannelEventListener;
import com.kuparts.dubbotcc.supervise.TNetEvent;
import com.kuparts.dubbotcc.supervise.net.NetServer;
import com.kuparts.dubbotcc.supervise.propety.Context;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 服务中间层处理
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public abstract class AbstractNetServer implements NetServer {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractNetServer.class);
    //本机服务地址
    protected String localAddress;
    //本机端口
    protected Context context;

    protected TChannelEventListener listener;

    private final BlockingQueue<TNetEvent> QUEUE;

    private final ExecutorService POOL = Executors.newSingleThreadExecutor();

    //是否停止张程
    private volatile boolean isRun = true;

    //初始化网络服务
    public AbstractNetServer(Context context, TChannelEventListener listener) {
        this.context = context;
        this.listener = listener;
        QUEUE = new LinkedBlockingQueue<>(context.getListenerMax());
    }

    /**
     * 启动服务
     *
     * @throws Exception
     */
    protected abstract void startServer() throws Exception;

    /**
     * 停止服务
     */
    protected void stopServer() {
        isRun = false;//停止运行
        if (isRun) {
            if (!POOL.isShutdown()) {
                POOL.shutdown();
            }
        }
    }

    @Override
    public void start() {
        Assert.notNull(context);
        Assert.notNull(listener);
        try {
            startServer();
            //启动线程监听
            POOL.execute(new Worker());
        } catch (Exception e) {
            LOG.error("start dubbotcc error system exit");
            isRun = false;
            stopServer();//停止线程
            System.exit(1);
        }
    }

    /**
     * 增加一个处理事件到队列中..
     *
     * @param event
     */
    protected void addEnevt(TNetEvent event) throws TccException {
        try {
            if (isRun) {
                QUEUE.put(event);
            } else {
                throw new TccException("queue stop....");
            }
        } catch (InterruptedException e) {
            LOG.error("add event error");
            throw new TccException("add event error", e.getCause());
        }
    }

    /**
     * 事件监听队列处理
     */
    class Worker implements Runnable {
        @Override
        public void run() {
            //是否可以运行
            TChannelEventListener listener = AbstractNetServer.this.listener;
            while (isRun && !Thread.currentThread().isInterrupted()) {
                try {

                    TNetEvent event = QUEUE.take();
                    switch (event.getType()) {
                        case CONNECT:
                            listener.channelActive(event.getAddress(), event.getChannel());
                            break;
                        case READ:
                            listener.channelReadComplete(event.getAddress(), event.getChannel());
                            break;
                        case CLOSE:
                            listener.channelClose(event.getAddress(), event.getChannel());
                            break;
                        case EXCEPTION:
                            listener.exceptionCaught(event.getAddress(), event.getChannel());
                            break;
                        default:
                            LOG.error("send listener error");
                            break;
                    }
                } catch (InterruptedException e) {
                    LOG.error("send listener error,Have been discarded");
                } catch (Exception e) {
                    LOG.error("send listener error,Have been discarded");
                }
            }
        }
    }
}
