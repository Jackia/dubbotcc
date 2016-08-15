package com.kuparts.dubbotcc.core.dispatch.propety;

import com.alibaba.dubbo.common.URL;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.commons.utils.DateUtils;
import com.kuparts.dubbotcc.commons.utils.UrlUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private long number;
    //参与者名称
    private String name;
    //参与者地址
    private String local;
    //参与者端口号
    private int port;
    //参与者发起时间
    private long runTime;//注册时间
    //节点类型
    private ActorType type;
    //状态
    private ActorStatus status;

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

    public Actor() {
        this(null, null, 0, DateUtils.nowEpochSecond());
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

    public ActorType getType() {
        return type;
    }

    public void setType(ActorType type) {
        this.type = type;
    }

    public ActorStatus getStatus() {
        return status;
    }

    public void setStatus(ActorStatus status) {
        this.status = status;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public void setRunTime(long runTime) {
        this.runTime = runTime;
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
        params.put("runTime", String.valueOf(runTime));
        params.put("number", String.valueOf(number));
        params.put("status", status.name());
        params.put("type", type.name());
        Map<String, String> defauls = new HashMap<>();
        defauls.put("port", String.valueOf(port));
        defauls.put("host", local);
        defauls.put("path", name);
        return UrlUtils.parseURL(params, defauls);
    }

    /**
     * url 转换为actor对象
     *
     * @param url url
     * @return actor对象
     */
    public static Actor parseActor(String url) {
        Assert.notNull(url);
        URL afterUrl = URL.valueOf(url);
        Actor actor = new Actor();
        actor.setLocal(afterUrl.getHost());
        actor.setName(afterUrl.getPath());
        actor.setNumber(Long.parseLong(afterUrl.getParameter("number")));
        actor.setRunTime(Long.parseLong(afterUrl.getParameter("runTime")));
        actor.setPort(afterUrl.getPort());
        actor.setStatus(ActorStatus.valueOf(afterUrl.getParameter("status")));
        actor.setType(ActorType.valueOf(afterUrl.getParameter("type")));
        return actor;
    }

    /**
     * 将一组URL转换成Actor对象
     *
     * @param urls 一组urls
     * @return 一组actor对象
     */
    public static List<Actor> parseActor(List<String> urls) {
        List<Actor> existActors = new ArrayList<>();
        if (urls != null) {
            urls.forEach(e -> {
                String u = URL.decode(e);
                Actor actor = parseActor(u);
                existActors.add(actor);
            });
        }
        return existActors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Actor actor = (Actor) o;

        if (number != actor.number) {
            return false;
        }
        if (!name.equals(actor.name)) {
            return false;
        }
        return local.equals(actor.local);

    }

    @Override
    public int hashCode() {
        int result = (int) (number ^ number >>> 32);
        result = 31 * result + name.hashCode();
        result = 31 * result + local.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", local='" + local + '\'' +
                ", port=" + port +
                ", runTime=" + runTime +
                ", type=" + type +
                ", status=" + status +
                '}';
    }
}
