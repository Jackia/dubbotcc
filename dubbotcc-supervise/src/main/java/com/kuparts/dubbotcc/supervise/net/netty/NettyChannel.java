package com.kuparts.dubbotcc.supervise.net.netty;

import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.supervise.TChannel;
import com.kuparts.dubbotcc.supervise.TChannelHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

import java.net.SocketAddress;

/**
 * netty通道实现
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class NettyChannel implements TChannel {

    private Channel channel;

    public NettyChannel(ChannelHandlerContext channelHandlerContext) {
        Assert.notNull(channelHandlerContext);
        this.channel = channelHandlerContext.channel();
    }

    public NettyChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public SocketAddress remoteAddress() {
        return channel.remoteAddress();
    }

    @Override
    public SocketAddress localAddress() {
        return channel.localAddress();
    }

    @Override
    public boolean isConnected() {
        return channel.isActive();
    }

    @Override
    public boolean isOpened() {
        return channel.isOpen();
    }

    @Override
    public boolean isClose() {
        return !channel.isOpen();
    }

    @Override
    public TChannelHandler writeAndFlush(Object message) {
        ChannelFuture future = channel.writeAndFlush(message);
        return new NettyChannelHandler(future);
    }

    @Override
    public TChannelHandler close() {
        return new NettyChannelHandler(channel.close());
    }
}
