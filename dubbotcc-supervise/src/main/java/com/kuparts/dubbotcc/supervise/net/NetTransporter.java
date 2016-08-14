package com.kuparts.dubbotcc.supervise.net;

import com.alibaba.dubbo.common.extension.SPI;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@SPI("netty")
public interface NetTransporter {
    /**
     * 获取客户端
     *
     * @return 服务通讯客户端
     */
    NetClient netClient();

    /**
     * 获取服务端
     *
     * @return 服务通讯服务端
     */
    NetServer netServer();
}
