package com.kuparts.dubbotcc.supervise.net.netty;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.supervise.TChannel;
import com.kuparts.dubbotcc.supervise.api.codec.Codec;
import com.kuparts.dubbotcc.supervise.net.NetHelper;
import com.kuparts.dubbotcc.supervise.propety.InvokeCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteBuffer;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class NettyCoderFactory {

    protected static final Logger LOG = LoggerFactory.getLogger(NettyCoderFactory.class);
    private final Codec codec;

    public NettyCoderFactory(Codec codec) {
        this.codec = codec;
    }

    public ChannelHandler getEnCoder() {
        return new NettyEnCoder();
    }

    public ChannelHandler getDeCoder() {
        return new NettyDeCoder();
    }

    /**
     * 加码
     */
    @ChannelHandler.Sharable
    public class NettyEnCoder extends MessageToByteEncoder<InvokeCommand> {

        @Override
        protected void encode(ChannelHandlerContext ctx, InvokeCommand msg, ByteBuf out) throws Exception {
            System.out.println("加码了.........." + ctx.channel().localAddress() + "," + msg);
            if (msg == null) {
                return;
            }
            try {
                ByteBuffer buffer = codec.encodec(msg);
                out.writeBytes(buffer);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
                LOG.info("encode invokeCommand error..," + ex.getMessage());
                TChannel channel = new NettyChannel(ctx.channel());
                NetHelper.closeChannel(channel);
            }
        }
    }

    public class NettyDeCoder extends LengthFieldBasedFrameDecoder {
        public NettyDeCoder() {
            super(1024, 0, 4, 0, 0);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
            System.out.println("解码了.........." + ctx.channel().localAddress());
            if (in.capacity() > 1024) {
                return null;
            }
            ByteBuf out = (ByteBuf) super.decode(ctx, in);
            if (out == null) {
                return null;
            }
            byte[] ds = new byte[out.capacity()];
            out.getBytes(0, ds);
            ByteBuffer buffer = ByteBuffer.wrap(ds);
            return codec.decodec(buffer);
        }
    }
}
