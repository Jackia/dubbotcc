package com.kuparts.dubbotcc.core.dispatch.net.netty;

import com.kuparts.dubbotcc.core.dispatch.TChannel;
import com.kuparts.dubbotcc.core.dispatch.TChannelFuture;
import io.netty.channel.ChannelFuture;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class NettyChannelFuture implements TChannelFuture {

    ChannelFuture channelFuture;

    public NettyChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    @Override
    public boolean isConnected() {
        return channelFuture.channel() != null && channelFuture.channel().isActive();
    }

    @Override
    public TChannel getChannel() {
        return new NettyChannel(channelFuture.channel());
    }

    @Override
    public boolean awaitUninterruptibly(long timeoutMillis) {
        return channelFuture.awaitUninterruptibly(timeoutMillis);
    }

    @Override
    public boolean isDone() {
        return channelFuture.isDone();
    }

    @Override
    public Throwable cause() {
        return channelFuture.cause();
    }
}
