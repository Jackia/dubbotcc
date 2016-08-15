package com.kuparts.dubbotcc.core.dispatch;

/**
 * 事务监听
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public interface TChannelEventListener {
    /**
     * 通道初始化事件,这是同步
     *
     * @param address  IP
     * @param tChannel 通道
     * @throws Exception
     */
    void onConnection(final String address, final TChannel tChannel) throws Exception;

    /**
     * 通道激活
     *
     * @param address
     * @param tChannel
     * @throws Exception
     */
    void onChannelActive(final String address, final TChannel tChannel) throws Exception;

    /**
     * 通道关闭事件
     *
     * @param address
     * @param tChannel
     * @throws Exception
     */
    void onChannelClose(final String address, final TChannel tChannel) throws Exception;

    /**
     * 读取完成
     *
     * @param address
     * @param tChannel
     * @throws Exception
     */
    void onChannelReadComplete(final String address, final TChannel tChannel) throws Exception;

    /**
     * 发生异常
     *
     * @param address
     * @param tChannel
     * @throws Exception
     */
    void onExceptionCaught(final String address, final TChannel tChannel) throws Exception;

    /**
     * 通道空闲
     *
     * @param address
     * @param tChannel
     * @throws Exception
     */
    void onChannelIdle(final String address, final TChannel tChannel) throws Exception;
}
