package com.kuparts.dubbotcc.supervise.support;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.commons.exception.TccException;
import com.kuparts.dubbotcc.supervise.TChannel;
import com.kuparts.dubbotcc.supervise.TChannelEventListener;
import com.kuparts.dubbotcc.supervise.TNetEvent;
import com.kuparts.dubbotcc.supervise.propety.Context;
import com.kuparts.dubbotcc.supervise.propety.InvokeCommand;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public abstract class AbstractNet implements Notify {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractNet.class);
    //本机端口
    public Context context;

    public final TChannelEventListener listener;

    private final BlockingQueue<TNetEvent> QUEUE;

    private final ExecutorService POOL = Executors.newSingleThreadExecutor();

    private final Mediator mediator;

    //是否停止张程
    protected volatile boolean isRun = true;

    protected AbstractNet(Context context, TChannelEventListener listener, Mediator mediator) {
        this.context = context;
        this.listener = listener;
        this.mediator = mediator;
        QUEUE = new LinkedBlockingQueue<>(context.getListenerMax());
    }

    /**
     * 增加一个处理事件到队列中..异步命令
     *
     * @param event
     */
    protected void asyncEvent(TNetEvent event) throws TccException {
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
     * 发布一个同步命令
     *
     * @param event
     * @throws TccException
     */
    protected void syncEvent(TNetEvent event) throws TccException {
        if (listener != null) {
            try {

                switch (event.getType()) {
                    case INIT:
                        listener.init(event.getAddress(), event.getChannel());
                        break;
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
                    case IDLE:
                        listener.channelIdle(event.getAddress(), event.getChannel());
                        break;
                    default:
                        LOG.error("send listener error");
                        break;
                }
            } catch (Exception e) {
                LOG.error("send listener error,Have been discarded");
                throw new TccException("send listener error,Have been discarded");
            }
        }
    }

    /**
     * 启动队列命令监听
     */
    public void excute() {
        if (listener == null) {
            return;
        }
        //启动线程监听
        POOL.execute(new Worker());
    }

    /**
     * 发布命令
     *
     * @param channel 通道
     * @param command 命令
     */
    protected abstract void processReleaseCommand(TChannel channel, InvokeCommand command) throws Exception;

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

    /**
     * 事件监听队列处理
     */
    class Worker implements Runnable {
        @Override
        public void run() {
            //是否可以运行
            TChannelEventListener listener = AbstractNet.this.listener;
            while (isRun && !Thread.currentThread().isInterrupted()) {
                try {

                    TNetEvent event = QUEUE.take();
                    switch (event.getType()) {
                        case INIT:
                            listener.init(event.getAddress(), event.getChannel());
                            break;
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
                        case IDLE:
                            listener.channelIdle(event.getAddress(), event.getChannel());
                            break;
                        default:
                            LOG.error("send listener error");
                            break;
                    }
                } catch (Exception e) {
                    LOG.error("send listener error,Have been discarded");
                }
            }
        }
    }
}
