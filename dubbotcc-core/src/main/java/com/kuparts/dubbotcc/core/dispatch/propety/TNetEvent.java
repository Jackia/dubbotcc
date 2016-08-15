package com.kuparts.dubbotcc.core.dispatch.propety;

import com.kuparts.dubbotcc.core.dispatch.TChannel;

/**
 * 事件指令
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TNetEvent {
    //事件类型
    private EventType type;
    //发生的远程地址
    private String address;
    //通道
    private TChannel channel;

    public TNetEvent(EventType type, String address, TChannel channel) {
        this.type = type;
        this.address = address;
        this.channel = channel;
    }

    public EventType getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public TChannel getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return "TNetEvent{" +
                "type=" + type.name() +
                ", address='" + address + '\'' +
                ", channel=" + channel +
                '}';
    }
}

