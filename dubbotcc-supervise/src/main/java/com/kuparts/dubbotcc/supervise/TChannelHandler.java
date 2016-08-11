package com.kuparts.dubbotcc.supervise;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public interface TChannelHandler {
    /**
     * 增加监听
     *
     * @param listener
     * @throws Exception
     */
    void addListener(TChannelHandlerListener listener) throws Exception;
}
