package com.kuparts.dubbotcc.commons.config;

/**
 * 扩展配置信息
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TccExtConfig extends TccConfig {

    //本地IP地址
    private String localAddress;
    //IP地址
    private int port;
    /**
     * 回滚队列大小
     */
    private int rollbackQueueMax;
    /**
     * 监听回滚队列线程数
     */
    private int rollbackThreadMax;
    //mongo数据库设置
    public String mongoDbName;
    //mongo数据库URL
    public String mongoDbUrl;
    //mongo数据库用户名
    public String mongoUserName;
    //mongo数据库密码
    public String mongoUserPwd;

    //------------get and set method ---------

    public int getRollbackQueueMax() {
        return rollbackQueueMax;
    }

    public void setRollbackQueueMax(int rollbackQueueMax) {
        this.rollbackQueueMax = rollbackQueueMax;
    }

    public int getRollbackThreadMax() {
        return rollbackThreadMax;
    }

    public void setRollbackThreadMax(int rollbackThreadMax) {
        this.rollbackThreadMax = rollbackThreadMax;
    }

    public String getMongoDbName() {
        return mongoDbName;
    }

    public void setMongoDbName(String mongoDbName) {
        this.mongoDbName = mongoDbName;
    }

    public String getMongoDbUrl() {
        return mongoDbUrl;
    }

    public void setMongoDbUrl(String mongoDbUrl) {
        this.mongoDbUrl = mongoDbUrl;
    }

    public String getMongoUserName() {
        return mongoUserName;
    }

    public void setMongoUserName(String mongoUserName) {
        this.mongoUserName = mongoUserName;
    }

    public String getMongoUserPwd() {
        return mongoUserPwd;
    }

    public void setMongoUserPwd(String mongoUserPwd) {
        this.mongoUserPwd = mongoUserPwd;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
