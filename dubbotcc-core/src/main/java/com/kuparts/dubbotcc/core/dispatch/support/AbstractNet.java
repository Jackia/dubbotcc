package com.kuparts.dubbotcc.core.dispatch.support;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.google.common.util.concurrent.FutureCallback;
import com.kuparts.dubbotcc.commons.bean.BeanServiceUtils;
import com.kuparts.dubbotcc.commons.exception.TccException;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.commons.utils.MapEntity;
import com.kuparts.dubbotcc.core.dispatch.Command;
import com.kuparts.dubbotcc.core.dispatch.TChannel;
import com.kuparts.dubbotcc.core.dispatch.TChannelEventListener;
import com.kuparts.dubbotcc.core.dispatch.propety.CommandType;
import com.kuparts.dubbotcc.core.dispatch.propety.Context;
import com.kuparts.dubbotcc.core.dispatch.propety.InvokeCommand;
import com.kuparts.dubbotcc.core.dispatch.propety.TNetEvent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public abstract class AbstractNet implements Notify {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractNet.class);
    //本机端口
    public Context context;
    //事件监听
    public final TChannelEventListener listener;
    //事件接收队列
    private BlockingQueue<TNetEvent> queue;
    //事件接收线程
    private final ExecutorService POOL = Executors.newSingleThreadExecutor();
    //回调中心
    private final Mediator mediator;
    //是否停止
    protected volatile boolean isRun = true;
    //命令初始化
    private Map<String, Command> commands;

    protected AbstractNet(Context context, TChannelEventListener listener, Mediator mediator) {
        this.context = context;
        this.listener = listener;
        this.mediator = mediator;
        if (listener != null) {
            queue = new LinkedBlockingQueue<>(context.getListenerMax());
        }
        //初始化bean
        Map<String, Object> beans = BeanServiceUtils.getInstance()
                .getBeanWithAnnotation(CommandType.CommandTask.class);
        if (beans != null) {
            commands = beans.values().stream()
                    .filter(e -> e != null && e instanceof Command)
                    .map(n -> {
                        MapEntity<Command> entity = new MapEntity<>();
                        String key = n.getClass().getAnnotation(CommandType.CommandTask.class).value().name();
                        entity.setKey(key);
                        entity.setValue((Command) n);
                        return entity;
                    }).collect(Collectors.toConcurrentMap(MapEntity::getKey, MapEntity::getValue));
        }
    }

    /**
     * 增加一个处理事件到队列中..
     * 异步命令
     *
     * @param event
     */
    protected void asyncEvent(TNetEvent event) throws TccException {
        try {
            if (isRun && queue != null) {
                queue.put(event);
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
                Assert.notNull(event);
                Assert.notNull(event.getType());
                invokeEvent(event);
            } catch (Exception e) {
                LOG.error("send listener error,Have been discarded");
                throw new TccException("send listener error,Have been discarded");
            }
        }
    }

    /**
     * 通过代理执行命令
     * 如果是异步调用总是返回false
     * 结果会通过回调函数处理
     *
     * @param ch       当前操作通道
     * @param command  命令
     * @param callback 传值表示异步调用,传空
     * @return 是否成功执行
     */
    protected boolean proxy(TChannel ch, InvokeCommand command, FutureCallback<Boolean> callback) {
        Assert.notNull(command);
        Assert.notNull(command.getCommandType());
        Assert.notNull(command.getTargetActor());
        if (callback != null) {
            CompletableFuture.runAsync(() -> {
                try {
                    boolean flag = callCommand(ch, command);
                    callback.onSuccess(flag);
                } catch (Exception e) {
                    callback.onFailure(e);
                }
            });
        } else {
            try {
                return callCommand(ch, command);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * 真实结果调用
     *
     * @param ch
     * @param command
     * @return
     */
    private Boolean callCommand(TChannel ch, InvokeCommand command) throws Exception {
        if (commands != null) {
            String cs = CommandType.parse(command.getCommandType()).name();
            LOG.debug("call command start:" + cs);
            Command commandInstance = commands.get(cs);
            Assert.notNull(commandInstance);
            return commands.get(cs).receive(ch, command.getTargetActor());
        }
        return false;
    }

    /**
     * 调用事件
     *
     * @param event
     * @throws NoSuchMethodException     没有找到方未能
     * @throws IllegalAccessException
     * @throws InvocationTargetException 目标有借
     */
    private void invokeEvent(TNetEvent event) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = listener.getClass().getMethod(event.getType().getEventName(), String.class, TChannel.class);
        method.invoke(listener, event.getAddress(), event.getChannel());
    }

    /**
     * 启动队列命令监听
     */
    public void execute() {
        if (listener == null) {
            return;
        }
        if (queue == null) {
            return;
        }
        //启动线程监听
        POOL.execute(new Worker());
    }

    /**
     * 发布一个命到其它的相同的dubbo客户端
     *
     * @param channel 通道
     * @param command 命令
     */
    protected abstract void processReleaseCommand(TChannel channel, InvokeCommand command) throws Exception;

    /**
     * 停止服务
     */
    protected void stopServer() {
        if (isRun) {
            if (!POOL.isShutdown()) {
                POOL.shutdown();
            }
            isRun = false;//停止运行
        }
    }

    /**
     * 事件代理实现
     */
    private class EventProxy implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            LOG.info("call event start:" + method.getName());
            if (proxy instanceof TChannelEventListener) {
                method.invoke(proxy, args);
            }
            LOG.info("call event end:" + method.getName());
            return null;
        }

        /**
         * 事件调用
         *
         * @param tClass
         */
        public <T> T newProxyInstance(Class<T> tClass) {
            EventProxy porxy = new EventProxy();
            Class[] classes = {tClass};
            return (T) Proxy.newProxyInstance(tClass.getClassLoader(), classes, porxy);
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
                    TNetEvent event = queue.take();
                    invokeEvent(event);
                } catch (Exception e) {
                    LOG.error("send listener error,Have been discarded");
                }
            }
        }
    }
}
