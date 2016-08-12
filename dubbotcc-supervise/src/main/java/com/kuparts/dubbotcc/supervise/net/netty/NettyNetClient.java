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
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import java.net.InetAddress;
import java.net.SocketAddress;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class NettyNetClient extends AbstractNetClient {

    private NioEventLoopGroup workGroup = null;
    private DefaultEventExecutorGroup defaultEventExecutorGroup = null;
    private Bootstrap bootstrap;

    public NettyNetClient(Context context, TChannelEventListener listener) {
        super(context, listener);
        workGroup = new NioEventLoopGroup(1);
        bootstrap = new Bootstrap();
    }

    @Override
    public void asynInvoker() {

    }

    @Override
    public void synInvoker() {

    }

    @Override
    protected void startClient() {
        NettyCoderFactory coderFactory = new NettyCoderFactory(new DefaultCodec());
        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(
                Runtime.getRuntime().availableProcessors() << 1,
                new NamedThreadFactory("Netty_Dubbotcc_client_")
        );
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(
                                defaultEventExecutorGroup,
                                coderFactory.getDeCoder(),
                                coderFactory.getEnCoder(),
                                new IdleStateHandler(context.getReaderIdleTimeSeconds(), context.getWriterIdleTimeSeconds(), context.getServerChannelMaxIdleTimeSeconds()),
                                new NettyConnectHandler(),
                                new NettyClientHandler()
                        );
                    }
                });

    }

    @Override
    protected TChannel connection(InetAddress host, int port) {
        ChannelFuture future = this.bootstrap.connect(host, port);
        return new NettyChannel(future.channel());
    }

    class NettyConnectHandler extends ChannelDuplexHandler {
        @Override
        public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise future) throws Exception {
            String local = localAddress != null ? localAddress.toString() : "NONE";
            String remote = remoteAddress != null ? remoteAddress.toString() : "NONE";
            LOG.debug("client : connect  {" + local + "} => {" + remote + "}");
            super.connect(ctx, remoteAddress, localAddress, future);
            if (listener != null) {
                TNetEvent event = new TNetEvent(EventType.CONNECT, remote, new NettyChannel(ctx));
                addEvent(event);
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
                addEvent(event);
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
                addEvent(event);
            }
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                NettyChannel channel = new NettyChannel(ctx);
                String address = NetHelper.parseChannelRemoteAddr(channel);
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state().equals(IdleState.ALL_IDLE)) {
                    LOG.info("client close channel:" + channel.localAddress());
                    closeChannel(channel);
                }
                if (listener != null) {
                    TNetEvent tnetEvent = new TNetEvent(EventType.IDLE, address, channel);
                    addEvent(tnetEvent);
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
                addEvent(event);
            }
        }
    }


    /**
     * 命令处理
     */
    class NettyClientHandler extends SimpleChannelInboundHandler<InvokeCommand> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, InvokeCommand msg) throws Exception {
            processReleaseCommand(new NettyChannel(ctx.channel()), msg);
        }
    }
}
