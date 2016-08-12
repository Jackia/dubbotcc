package com.kuparts.dubbotcc.supervise;

/**
 * 事务监听
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public interface TChannelEventListener {

    void serverInit(final String address, final TChannel tChannel) throws Exception;

    void channelActive(final String address, final TChannel tChannel) throws Exception;

    void channelClose(final String address, final TChannel tChannel) throws Exception;

    void channelReadComplete(final String address, final TChannel tChannel) throws Exception;

    void exceptionCaught(final String address, final TChannel tChannel) throws Exception;

    void channelIdle(final String address, final TChannel tChannel) throws Exception;

}
