package com.kuparts.dubbotcc.supervise.support;

import com.kuparts.dubbotcc.supervise.net.NetClient;

/**
 * 服务客户端中间层处理
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public abstract class AbstractNetClient implements NetClient {
    //服务远程连接地址
    protected String remoteAddress;
    //远程ip地址
    protected int port;

    public AbstractNetClient(int port) {
        this.port = port;
    }
}
