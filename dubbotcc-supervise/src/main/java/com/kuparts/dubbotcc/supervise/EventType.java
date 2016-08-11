package com.kuparts.dubbotcc.supervise;

/**
 * 事务的类型
 */
public enum EventType {
    //连接
    CONNECT,
    //读取数据
    READ,
    //关闭
    CLOSE,
    //异常
    EXCEPTION
}
