package com.kuparts.dubbotcc.supervise.net.netty;

import com.alibaba.dubbo.common.utils.NamedThreadFactory;
import com.kuparts.dubbotcc.supervise.EventType;
import com.kuparts.dubbotcc.supervise.TChannel;
import com.kuparts.dubbotcc.supervise.TChannelEventListener;
import com.kuparts.dubbotcc.supervise.TNetEvent;
import com.kuparts.dubbotcc.supervise.api.codec.DefaultCodec;
import com.kuparts.dubbotcc.supervise.net.NetHelper;
import com.kuparts.dubbotcc.supervise.propety.Context;
import com.kuparts.dubbotcc.supervise.propety.InvokeCommand;
import com.kuparts.dubbotcc.supervise.support.AbstractNetClient;
import com.kuparts.dubbotcc.supervise.support.Mediator;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class NettyNetClient extends AbstractNetClient {

    private final NioEventLoopGroup workGroup;
    private DefaultEventExecutorGroup defaultEventExecutorGroup;
    private final Bootstrap bootstrap;

    public NettyNetClient(Context context, TChannelEventListener listener, Mediator mediator) {
        super(context, listener, mediator);
        workGroup = new NioEventLoopGroup(1);
        bootstrap = new Bootstrap();
    }

    @Override
    public void asynInvoker() {
        System.out.println("开始发送命令............");
    }

    @Override
    public void synInvoker() {

    }

    @Override
    protected void startClient() {
        NettyCoderFactory coderFactory = new NettyCoderFactory(new DefaultCodec());
        defaultEventExecutorGroup = new DefaultEventExecutorGroup(
                Runtime.getRuntime().availableProcessors() << 1,
                new NamedThreadFactory("Netty_Dubbotcc_client_")
        );
        NettyConnectHandler nettyConnectHandler = new NettyConnectHandler();
        NettyClientHandler nettyClientHandler = new NettyClientHandler();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(
                                defaultEventExecutorGroup,
                                coderFactory.getEnCoder(),
                                coderFactory.getDeCoder(),
//                                new IdleStateHandler(context.getReaderIdleTimeSeconds(), context.getWriterIdleTimeSeconds(), context.getServerChannelMaxIdleTimeSeconds()),
                                nettyConnectHandler,
                                nettyClientHandler
                        );
                    }
                });

    }

    @Override
    protected TChannel connection(String host, int port) {
        SocketAddress socketAddress = new InetSocketAddress(host, port);
        ChannelFuture future = null;
        try {
            future = bootstrap.connect(socketAddress).sync();
        } catch (InterruptedException e) {

        }
        return new NettyChannel(future.channel());
    }

    @ChannelHandler.Sharable
    public class NettyConnectHandler extends ChannelDuplexHandler {
        @Override
        public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise future) throws Exception {
            String local = localAddress != null ? localAddress.toString() : "NONE";
            String remote = remoteAddress != null ? remoteAddress.toString() : "NONE";
            LOG.debug("client : connect  {" + local + "} => {" + remote + "}");
            super.connect(ctx, remoteAddress, localAddress, future);
            if (listener != null) {
                TNetEvent event = new TNetEvent(EventType.CONNECT, remote, new NettyChannel(ctx));
                asyncEvent(event);
            }
        }

        @Override
        public void disconnect(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
            TChannel channel = new NettyChannel(future.channel());
            String remote = NetHelper.parseChannelRemoteAddr(channel);
            LOG.debug("disconnect remoteAddress:" + remote);
            super.disconnect(ctx, future);
            if (listener != null) {
                TNetEvent event = new TNetEvent(EventType.CLOSE, remote, new NettyChannel(ctx));
                asyncEvent(event);
            }
        }

        @Override
        public void close(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
            TChannel channel = new NettyChannel(future.channel());
            String remote = NetHelper.parseChannelRemoteAddr(channel);
            LOG.debug("close remoteAddress:" + remote);
            super.close(ctx, future);
            if (listener != null) {
                TNetEvent event = new TNetEvent(EventType.CLOSE, remote, new NettyChannel(ctx));
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
                    LOG.info("client close channel:" + channel.localAddress());
                    closeChannel(channel);
                }
                if (listener != null) {
                    TNetEvent tnetEvent = new TNetEvent(EventType.IDLE, address, channel);
                    asyncEvent(tnetEvent);
                }
            }
            super.userEventTriggered(ctx, evt);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            TChannel channel = new NettyChannel(ctx.channel());
            String remote = NetHelper.parseChannelRemoteAddr(channel);
            LOG.warn("exception,remoteAddress:" + remote);
            LOG.warn("exception,msg:" + cause.getMessage());
            super.exceptionCaught(ctx, cause);
            if (listener != null) {
                TNetEvent event = new TNetEvent(EventType.EXCEPTION, remote, channel);
                asyncEvent(event);
            }
        }
    }


    /**
     * 命令处理
     */
    @ChannelHandler.Sharable
    public class NettyClientHandler extends SimpleChannelInboundHandler<InvokeCommand> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, InvokeCommand msg) throws Exception {
            processReleaseCommand(new NettyChannel(ctx.channel()), msg);
        }

    }
}
