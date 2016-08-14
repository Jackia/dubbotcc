package com.kuparts.dubbotcc.supervise.propety;

import com.alibaba.dubbo.common.URL;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.commons.utils.DateUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
    private final ConcurrentHashMap<String, Actor> ACTORS;
    //当前本机注册者
    private volatile Actor currentActor;
    //主机节点,如果是主节点,这里记录都是关察者节点
    private volatile Actor masterActor;
    //注册时间
    private final long startTime;
    //监听的最大队列
    private int listenerMax = 2000;
    //所初始化序列号ID
    private String sid;
    //本机的IP地址
    private InetSocketAddress localAddress;
    //当前系统的namespace
    private String namespace;
    //系统名称与dubbo设置的一样
    private String name;
    //单例对象
    private static final Context context = new Context();
    //心跳检测
    private int readerIdleTimeSeconds = 0;
    private int writerIdleTimeSeconds = 0;
    private int serverChannelMaxIdleTimeSeconds = 120;
    //加锁
    private ReentrantReadWriteLock actorsPutLock = new ReentrantReadWriteLock();
    //system.propertis
    private final Properties properties;
    private final String propertiesPath = "/system.properties";

    private Context() {
        if (context != null) {
            throw new Error("error");
        }
        ACTORS = new ConcurrentHashMap<>();
        startTime = DateUtils.nowEpochSecond();
        //初始化system.propertis
        properties = new Properties();
        try {
            InputStream inputStream = Context.class.getResourceAsStream(propertiesPath);
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
        }
    }

    public static Context getContext() {
        return context;
    }

    /**
     * 增加一个Actor到当前上下文中
     *
     * @param actor
     */
    public void putActor(Actor actor) {
        Assert.notNull(actor);
        Assert.notNull(actor.getNumber());
        actorsPutLock.writeLock().lock();
        try {
            ACTORS.put(String.valueOf(actor.getNumber()), actor);
        } finally {
            actorsPutLock.writeLock().unlock();
        }
    }

    public String getProperties(String key) {
        Assert.notNull(properties);
        Assert.notNull(key);
        return properties.getProperty(key);
    }

    /**
     * 写入文件
     *
     * @param key
     * @param value
     */
    public void putPropertis(String key, String value) {
        Assert.notNull(key);
        Assert.notNull(value);
        Assert.notNull(properties);
        properties.setProperty(key, value);
        String file = Context.class.getResource(propertiesPath).getFile();
        file = URL.decode(file);
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            properties.store(stream, "");
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } finally {
            if (stream != null) {
                try {
                    stream.flush();
                    stream.close();
                } catch (IOException e) {

                }

            }
        }
    }

    /**
     * 保证同步性...
     * 保存节点
     *
     * @param actor
     */
    public void putIfActor(Actor actor) {
        try {
            actorsPutLock.writeLock().lock();
            ACTORS.putIfAbsent(String.valueOf(actor.getNumber()), actor);
        } finally {
            actorsPutLock.writeLock().unlock();
        }
    }

    public void putIfActor(List<Actor> actor) {
        try {
            actorsPutLock.writeLock().lock();
            actor.forEach(e -> ACTORS.putIfAbsent(String.valueOf(e.getNumber()), e));
        } finally {
            actorsPutLock.writeLock().unlock();
        }
    }

    /**
     * 删除在线节点
     *
     * @param actors
     */
    public void remove(List<Actor> actors) {
        try {
            actorsPutLock.writeLock().lock();
            actors.forEach(e -> ACTORS.remove(String.valueOf(e.getNumber())));
        } finally {
            actorsPutLock.writeLock().unlock();
        }
    }

    /**
     * 重置
     *
     * @param actors
     */
    public void reset(List<Actor> actors) {
        try {
            actorsPutLock.writeLock().lock();
            ACTORS.clear();
            actors.forEach(e -> ACTORS.put(String.valueOf(e.getNumber()), e));
        } finally {
            actorsPutLock.writeLock().unlock();
        }
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
        if (localAddress == null) {
            return "";
        }
        return localAddress.getHostString();
    }

    public int getLocalAddressPort() {
        if (localAddress == null) {
            return 0;
        }
        return localAddress.getPort();

    }

    public Actor getMasterActor() {
        return masterActor;
    }

    public void setMasterActor(Actor masterActor) {
        this.masterActor = masterActor;
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

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 判断当前节点是否为主节点
     *
     * @return
     */
    public boolean isMaster() {
        return currentActor.getType() == ActorType.MASTER;
    }

    /**
     * 获取当前节点数量
     *
     * @return 数量
     */
    public int getActorCount() {
        return ACTORS.size();
    }

    /**
     * 获取已经注册的节点集合
     *
     * @return 所有节点
     */
    public Set<Map.Entry<String, Actor>> getActors() {
        actorsPutLock.readLock().lock();
        Set<Map.Entry<String, Actor>> actors = null;
        try {
            actors = ACTORS.entrySet();
        } finally {
            actorsPutLock.readLock().unlock();
        }
        if (actors == null) {
            return Collections.emptySet();
        }
        return actors;
    }

    public boolean existActor(Actor actor) {
        return ACTORS.contains(actor);
    }


}
