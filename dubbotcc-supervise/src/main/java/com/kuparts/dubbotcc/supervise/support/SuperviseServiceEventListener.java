package com.kuparts.dubbotcc.supervise.support;

import com.kuparts.dubbotcc.supervise.TChannel;
import com.kuparts.dubbotcc.supervise.TChannelEventListener;

/**
 * 自定义事件监听器
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class SuperviseServiceEventListener implements TChannelEventListener {
    @Override
    public void serverInit(String address, TChannel tChannel) throws Exception {
        System.out.println("服务初始化成功..........................");
    }

    @Override
    public void channelActive(String address, TChannel tChannel) throws Exception {

    }

    @Override
    public void channelClose(String address, TChannel tChannel) throws Exception {

    }

    @Override
    public void channelReadComplete(String address, TChannel tChannel) throws Exception {

    }

    @Override
    public void exceptionCaught(String address, TChannel tChannel) throws Exception {

    }
}
