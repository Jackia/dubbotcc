package com.kuparts.dubbotcc.commons.config;

/**
 * 配置文件常量定义..
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TccExtConfigConstants {
    /**
     * 回滚队列大小
     */
    public static final String ROLLBACK_QUEUE_MAX = "rollbackQueueMax";
    public static final int DEFAULT_ROLLBACK_QUEUE_MAX = 5000;
    /**
     * 回滚队列接收线程
     */
    public static final String ROLLBACK_THREAD_MAX = "rollbackThreadMax";
    public static final int DEFAULT_ROLLBACK_THREAD_MAX = Runtime.getRuntime().availableProcessors() << 1;
    //====================================mongo===========================
    //mongo数据库设置
    public static final String MONGO_DATABASE_NAME = "mongoDbName";
    //mongo数据库URL
    public static final String MONGO_DATABASE_URL = "mongoDbUrl";
    //mongo数据库用户名
    public static final String MONGO_USER_NAME = "mongoUserName";
    //mongo数据库密码
    public static final String MONGO_USER_PWD = "mongoUserPwd";
    //=============================zookeeper==============================
    //zookeeper地址
    public static final String ZOOKEEPER_URL = "zookurl";
    //系统唯一标识
    public static final String APPLICATION = "application";
    //=============================重要配置=================================
}
