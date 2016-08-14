package com.kuparts.dubbotcc.supervise.net.netty;

import com.alibaba.dubbo.common.utils.NamedThreadFactory;
import com.kuparts.dubbotcc.supervise.EventType;
import com.kuparts.dubbotcc.supervise.TChannelEventListener;
import com.kuparts.dubbotcc.supervise.TNetEvent;
import com.kuparts.dubbotcc.supervise.api.codec.DefaultCodec;
import com.kuparts.dubbotcc.supervise.net.NetHelper;
import com.kuparts.dubbotcc.supervise.propety.Context;
import com.kuparts.dubbotcc.supervise.propety.InvokeCommand;
import com.kuparts.dubbotcc.supervise.support.AbstractNetServer;
import com.kuparts.dubbotcc.supervise.support.Mediator;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import java.net.InetSocketAddress;

/**
 * netty 服务启动
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class NettyNetServer extends AbstractNetServer {

    /**
     * 服务处理
     */
    private final NioEventLoopGroup workGroup;
    private final NioEventLoopGroup childGroup;
    private final ServerBootstrap server;
    private DefaultEventExecutorGroup defaultEventExecutorGroup;

    public NettyNetServer(Context context, TChannelEventListener listener, Mediator mediator) {
        super(context, listener, mediator);
        workGroup = new NioEventLoopGroup(1);
        childGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() << 1);
        server = new ServerBootstrap();
    }

    @Override
    protected void startServer() throws Exception {
        defaultEventExecutorGroup = new DefaultEventExecutorGroup(
                Runtime.getRuntime().availableProcessors() << 1,
                new NamedThreadFactory("Netty_Dubbotcc_Server_")
        );
        InetSocketAddress address = new InetSocketAddress(localAddress, 0);
        server.group(workGroup, childGroup)
                .option(ChannelOption.SO_BACKLOG, 65535)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .localAddress(address)
                .channel(NioServerSocketChannel.class)
                .childHandler(new TccChannelInitializer());
        //设置信息
        try {
            Channel channel = server.bind().sync().channel();
            int port = ((InetSocketAddress) channel.localAddress()).getPort();
            initServerLister(localAddress, port, new NettyChannel(channel));
            LOG.info("start netty service...ip:" + localAddress + ",port:" + port);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            LOG.error("start netty service error " + e.getMessage());
        } finally {
            workGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

    @Override
    protected void stopServer() {
        if (workGroup != null) {
            workGroup.shutdownGracefully();
        }
        if (childGroup != null) {
            childGroup.shutdownGracefully();
        }
        LOG.error("stop netty service");
        super.stopServer();

    }

    /**
     * 初始化通道
     */
    class TccChannelInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            NettyCoderFactory coderFactory = new NettyCoderFactory(new DefaultCodec());
            localAddress = ch.localAddress().getHostString();//设置本地IP地址
            ch.pipeline().addLast(
//                    defaultEventExecutorGroup,
                    coderFactory.getDeCoder(),
                    coderFactory.getEnCoder(),
//                    new IdleStateHandler(context.getReaderIdleTimeSeconds(), context.getWriterIdleTimeSeconds(), context.getServerChannelMaxIdleTimeSeconds()),
                    new NettyConnectHandler(),
                    new NettyServerHandler()
            );
        }
    }

    class NettyConnectHandler extends ChannelDuplexHandler {

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            String address = NetHelper.parseChannelRemoteAddr(new NettyChannel(ctx));
            LOG.debug("channelRegistered ip:" + address);
            super.channelRegistered(ctx);
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            String address = NetHelper.parseChannelRemoteAddr(new NettyChannel(ctx));
            LOG.debug("channelUnregistered ip:" + address);
            super.channelUnregistered(ctx);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            NettyChannel channel = new NettyChannel(ctx);
            String address = NetHelper.parseChannelRemoteAddr(channel);
            LOG.debug("channelActive ip:" + address);
            super.channelActive(ctx);
            if (listener != null) {
                TNetEvent event = new TNetEvent(EventType.CONNECT, address, channel);
                asyncEvent(event);
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            NettyChannel channel = new NettyChannel(ctx);
            String address = NetHelper.parseChannelRemoteAddr(channel);
            LOG.debug("channelActive ip:" + address);
            super.channelInactive(ctx);
            if (listener != null) {
                TNetEvent event = new TNetEvent(EventType.CLOSE, address, channel);
                asyncEvent(event);
            }
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                NettyChannel channel = new NettyChannel(ctx);
                String address = NetHelper.parseChannelRemoteAddr(channel);
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state() == IdleState.ALL_IDLE) {
                    NetHelper.closeChannel(channel);
                }
                if (listener != null) {
                    TNetEvent netEvent = new TNetEvent(EventType.IDLE, address, channel);
                    asyncEvent(netEvent);
                }
            }
            super.userEventTriggered(ctx, evt);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            NettyChannel channel = new NettyChannel(ctx);
            String address = NetHelper.parseChannelRemoteAddr(channel);
            LOG.debug("channelActive ip:" + address);
            super.exceptionCaught(ctx, cause);
            if (listener != null) {
                TNetEvent event = new TNetEvent(EventType.EXCEPTION, address, channel);
                asyncEvent(event);
            }
            NetHelper.closeChannel(channel);
        }
    }

    /**
     * 命令处理
     */
    @ChannelHandler.Sharable
    class NettyServerHandler extends SimpleChannelInboundHandler<InvokeCommand> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, InvokeCommand msg) throws Exception {
            processReleaseCommand(new NettyChannel(ctx.channel()), msg);
        }
    }
}
