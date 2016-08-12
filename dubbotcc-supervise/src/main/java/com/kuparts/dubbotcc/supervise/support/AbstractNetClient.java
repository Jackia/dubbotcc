package com.kuparts.dubbotcc.supervise.support;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.supervise.TChannel;
import com.kuparts.dubbotcc.supervise.TChannelEventListener;
import com.kuparts.dubbotcc.supervise.net.NetClient;
import com.kuparts.dubbotcc.supervise.propety.Context;
import com.kuparts.dubbotcc.supervise.propety.InvokeCommand;

import java.net.InetAddress;

/**
 * 服务客户端中间层处理
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public abstract class AbstractNetClient extends AbstractNet implements NetClient {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractNetClient.class);

    public AbstractNetClient(Context context, TChannelEventListener listener) {
        super(context, listener);
    }

    @Override
    protected void processReleaseCommand(TChannel channel, InvokeCommand command) {

    }

    /**
     * 关闭通道
     *
     * @param channel
     */
    protected void closeChannel(TChannel channel) {

    }

    /**
     * 初始化客户端
     */
    protected abstract void startClient();

    /**
     * 连接
     */
    protected abstract TChannel connection(InetAddress host, int port);


    @Override
    public void start() {
        startClient();
        excute();
    }
}
