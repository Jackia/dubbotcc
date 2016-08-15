package com.kuparts.dubbotcc.core.dispatch;

import java.net.SocketAddress;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public interface TChannel {

    SocketAddress remoteAddress();

    SocketAddress localAddress();

    boolean isConnected();

    boolean isOpened();

    boolean isClose();

    TChannelHandler writeAndFlush(Object message);

    TChannelHandler close();
}
