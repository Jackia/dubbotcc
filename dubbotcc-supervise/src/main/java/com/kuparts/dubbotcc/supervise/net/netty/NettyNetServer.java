package com.kuparts.dubbotcc.supervise.net.netty;

import com.kuparts.dubbotcc.supervise.TChannelEventListener;
import com.kuparts.dubbotcc.supervise.propety.Context;
import com.kuparts.dubbotcc.supervise.support.AbstractNetServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

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
    private NioEventLoopGroup workGroup = null;
    private NioEventLoopGroup childGroup = null;
    private ServerBootstrap server;

    public NettyNetServer(Context context, TChannelEventListener listener) {
        super(context, listener);
        workGroup = new NioEventLoopGroup(1);
        childGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() << 1);
    }

    @Override
    protected void startServer() throws Exception {
        /**
         * 启动服务程序
         */
        server = new ServerBootstrap();
        server.group(workGroup, childGroup)
                .option(ChannelOption.SO_BACKLOG, 65535)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .channel(NioServerSocketChannel.class)
                .childHandler(new TccChannelInitializer());
        //设置信息
        try {
            Channel channel = server.bind(context.getPort()).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            LOG.error("start netty service error " + e.getMessage());
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
            localAddress = ch.localAddress().getHostString();//设置本地IP地址
//            ch.pipeline().addLast()
        }
    }
}
