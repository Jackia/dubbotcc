package com.kuparts.dubbotcc.core.dispatch.propety;

/**
 * 事务的类型
 */
public enum EventType {
    //初始化
    INIT("onConnection"),
    //连接
    CONNECT("onChannelActive"),
    //读取数据
    READ("onChannelReadComplete"),
    //关闭
    CLOSE("onChannelClose"),
    //异常
    EXCEPTION("onExceptionCaught"),
    //闲置
    IDLE("onChannelIdle");
    private String eventName;

    EventType(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }
}
