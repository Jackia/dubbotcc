package com.kuparts.dubbotcc.supervise.propety;

import com.alibaba.dubbo.common.URL;
import com.google.common.base.Joiner;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.commons.utils.DateUtils;
import com.kuparts.dubbotcc.commons.utils.UrlUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 事务参与者
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class Actor implements Serializable {
    //序号每个相同事务属性的参与者的唯一编号
    private String number;
    //参与者名称
    private String name;
    //参与者地址
    private String local;
    //参与者端口号
    private int port;
    //参与者发起时间
    private long runTime;//注册时间
    //节点类型
    private String type;
    //状态
    private String status;

    /**
     * @param name    参与者名称
     * @param local   参与者地址
     * @param port    参与端口号
     * @param runTime 参与者时间
     */
    public Actor(String name, String local, int port, long runTime) {
        this.name = name;
        this.local = local;
        this.port = port;
        this.runTime = runTime;
    }

    public Actor(String name, String local, int port) {
        this(name, local, port, DateUtils.nowEpochSecond());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRunTime(long runTime) {
        this.runTime = runTime;
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

    public long getRunTime() {
        return runTime;
    }

    public String getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * 获取url地址
     *
     * @return url地址
     */
    public URL getURL() {
        Assert.notNull(name);
        Assert.notNull(local);
        Assert.checkConditionArgument(port >= 0, "port is error");
        Map<String, String> params = new HashMap<>();
        params.put("runTime", String.valueOf(this.runTime));
        Map<String, String> defauls = new HashMap<>();
        defauls.put("port", String.valueOf(this.port));
        defauls.put("host", this.local);
        defauls.put("path", this.name);
        defauls.put("status", this.status);
        defauls.put("type", this.type);
        return UrlUtils.parseURL(params, defauls);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Actor actor = (Actor) o;

        if (port != actor.port) return false;
        if (runTime != actor.runTime) return false;
        if (name != null ? !name.equals(actor.name) : actor.name != null) return false;
        return local != null ? local.equals(actor.local) : actor.local == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (local != null ? local.hashCode() : 0);
        result = 31 * result + port;
        result = 31 * result + (int) (runTime ^ (runTime >>> 32));
        return result;
    }

}

/**
 * 类型
 */
enum ActorType {
    MASTER("master"),
    SLAVE("slave"),
    OBSERVE("observe");
    private String typeStr;

    ActorType(String str) {
        this.typeStr = str;
    }

    public String getTypeStr() {
        return typeStr;
    }
}

/**
 * 状态
 */
enum ActorStatus {
    RUNING("runing"),
    CLOSED("closed");
    private String statusStr;

    ActorStatus(String str) {
        this.statusStr = str;
    }

    public String getTypeStr() {
        return statusStr;
    }
}
