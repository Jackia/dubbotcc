package com.kuparts.dubbotcc.supervise.net.netty;

import com.kuparts.dubbotcc.supervise.TChannelHandler;
import com.kuparts.dubbotcc.supervise.TChannelHandlerListener;
import com.kuparts.dubbotcc.supervise.TFuture;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * 通道处理的Handler
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class NettyChannelHandler implements TChannelHandler {

    ChannelFuture channelFuture;

    public NettyChannelHandler(ChannelFuture future) {
        this.channelFuture = future;
    }

    @Override
    public void addListener(TChannelHandlerListener listener) throws Exception {
        channelFuture.addListener((ChannelFutureListener) future -> {
            listener.operationComplete(new TFuture() {
                @Override
                public boolean isSuccessfully() {
                    return future.isSuccess();
                }

                @Override
                public Throwable cause() {
                    return future.cause();
                }
            });
        });
    }
}
