package com.kuparts.dubbotcc.core.dispatch.net.netty;

import com.kuparts.dubbotcc.core.dispatch.TChannelHandler;
import com.kuparts.dubbotcc.core.dispatch.TChannelHandlerListener;
import com.kuparts.dubbotcc.core.dispatch.TFuture;
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
