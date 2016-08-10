package com.kuparts.dubbotcc.supervise.propety;

import com.google.common.base.Joiner;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.commons.utils.DateUtils;
import com.kuparts.dubbotcc.commons.utils.UrlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 事务参与者
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class Actor {
    //参与者名称
    private String name;
    //参与者地址
    private String local;
    //参与者端口号
    private int port;
    //参与者发起时间
    private long rtime;//注册时间
    //节点类型
    private String type;

    /**
     * @param name  参与者名称
     * @param local 参与者地址
     * @param port  参与端口号
     * @param rtime 参与者时间
     */
    public Actor(String name, String local, int port, long rtime) {
        this.name = name;
        this.local = local;
        this.port = port;
        this.rtime = rtime;
    }

    public Actor(String name, String local, int port) {
        this(name, local, port, DateUtils.nowEpochSecond());
    }

    public void setRtime(long rtime) {
        this.rtime = rtime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getLocal() {
        return local;
    }

    public int getPort() {
        return port;
    }

    public long getRtime() {
        return rtime;
    }

    public String getType() {
        return type;
    }

    /**
     * 获取url地址
     *
     * @return url地址
     */
    public String getUrlStr() {
        Assert.notNull(name);
        Assert.notNull(local);
        Assert.checkConditionArgument(port >= 0, "port is error");
        Map<String, String> params = new HashMap<>();
        params.put("rtime", String.valueOf(this.rtime));
        Map<String, String> defauls = new HashMap<>();
        defauls.put("port", String.valueOf(this.port));
        defauls.put("host", this.local);
        defauls.put("path", this.name);
        return UrlUtils.parseURL(params, defauls).toFullString();
    }

    /**
     * 获取nameSpace
     *
     * @return nameSpace字符串
     */
    public String nameSpace() {
        return Joiner.on(".").join(new String[]{name});
    }

    @Override
    public String toString() {
        return "Actor{" +
                "name='" + name + '\'' +
                ", local='" + local + '\'' +
                ", port=" + port +
                ", rtime=" + rtime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Actor actor = (Actor) o;

        if (port != actor.port) return false;
        if (rtime != actor.rtime) return false;
        if (name != null ? !name.equals(actor.name) : actor.name != null) return false;
        return local != null ? local.equals(actor.local) : actor.local == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (local != null ? local.hashCode() : 0);
        result = 31 * result + port;
        result = 31 * result + (int) (rtime ^ (rtime >>> 32));
        return result;
    }
}
