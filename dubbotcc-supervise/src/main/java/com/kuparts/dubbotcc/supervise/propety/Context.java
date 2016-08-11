package com.kuparts.dubbotcc.supervise.propety;

import com.alibaba.dubbo.common.URL;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.commons.utils.DateUtils;

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
    //本机IP地址
    private String address;
    //启动的port
    private int port;
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
    //单例对象
    private static final Context context = new Context();

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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
}
