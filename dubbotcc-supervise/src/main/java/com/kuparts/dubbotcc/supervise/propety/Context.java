package com.kuparts.dubbotcc.supervise.propety;

import com.alibaba.dubbo.common.URL;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.commons.utils.DateUtils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监管系统运行时数据
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class Context {
    //当前注册地址
    private URL rigisterUrl;
    //当前同一类型的注册者
    private final Map<String, Actor> ACTORS;
    //当前本机注册者
    private Actor currentActor;
    //注册时间
    private long startTime;
    //监听的最大队列
    private int listenerMax = 2000;
    //所初始化序列号ID
    private String sid;
    //本机的IP地址
    private InetSocketAddress localAddress;
    //单例对象
    private static final Context context = new Context();
    //心跳检测
    private int readerIdleTimeSeconds = 0;
    private int writerIdleTimeSeconds = 0;
    private int serverChannelMaxIdleTimeSeconds = 120;

    private Context() {
        if (context != null) {
            throw new Error("error");
        }
        ACTORS = new ConcurrentHashMap<>();
        this.startTime = DateUtils.nowEpochSecond();
    }

    public static Context getContext() {
        return context;
    }

    /**
     * 增加一个Actor到当前上下文中
     *
     * @param actor
     */
    public void addActor(Actor actor) {
        Assert.notNull(actor);
        Assert.notNull(actor.getNumber());
        ACTORS.put(actor.getNumber(), actor);
    }

    public int getListenerMax() {
        return listenerMax;
    }

    public void setListenerMax(int listenerMax) {
        this.listenerMax = listenerMax;
    }

    public InetAddress getLocalAddress() {
        return localAddress.getAddress();
    }

    public void setLocalAddress(InetSocketAddress localAddress) {
        this.localAddress = localAddress;
    }

    public URL getRigisterUrl() {
        return rigisterUrl;
    }

    public void setRigisterUrl(URL rigisterUrl) {
        this.rigisterUrl = rigisterUrl;
    }

    public Actor getCurrentActor() {
        return currentActor;
    }

    public void setCurrentActor(Actor currentActor) {
        this.currentActor = currentActor;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getLocalAddressString() {
        if (localAddress == null) return "";
        return localAddress.getHostString();
    }

    public int getLocalAddressPort() {
        if (localAddress == null) return 0;
        return localAddress.getPort();

    }

    public int getReaderIdleTimeSeconds() {
        return readerIdleTimeSeconds;
    }

    public int getWriterIdleTimeSeconds() {
        return writerIdleTimeSeconds;
    }

    public int getServerChannelMaxIdleTimeSeconds() {
        return serverChannelMaxIdleTimeSeconds;
    }
}
