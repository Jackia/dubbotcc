package com.kuparts.dubbotcc.supervise.support;

import com.kuparts.dubbotcc.supervise.TChannel;
import com.kuparts.dubbotcc.supervise.api.Supervise;

/**
 * 自定义事件监听器
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class SuperviseServiceEventListener extends AbstractServiceListener {

    public SuperviseServiceEventListener(Supervise supervise) {
        super(supervise);
    }

    @Override
    public void channelIdle(String address, TChannel tChannel) throws Exception {

    }

    @Override
    public void init(String address, TChannel tChannel) throws Exception {
        init(0, address, tChannel);
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
