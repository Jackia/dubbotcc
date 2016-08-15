package com.kuparts.dubbotcc.core.dispatch.net;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.core.dispatch.TChannel;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class NetHelper {

    protected static final Logger LOG = LoggerFactory.getLogger(NetHelper.class);

    /**
     * 获取本机所有IP
     *
     * @return 当前活动网卡的所有IP地址
     */
    public static Set<String> getlocalAddress() {
        Set<String> locals = new HashSet<>();
        try {
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                }
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = (InetAddress) addresses.nextElement();
                    if (ip instanceof Inet4Address) {
                        locals.add(ip.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            LOG.info("not found effective IP address....," + e.getMessage());
        }
        return locals;
    }

    public static String parseChannelRemoteAddr(final TChannel channel) {
        if (channel == null) {
            return "";
        }
        final SocketAddress remote = channel.remoteAddress();
        final String addr = remote != null ? remote.toString() : "";

        if (!addr.isEmpty()) {
            int index = addr.lastIndexOf("/");
            if (index >= 0) {
                return addr.substring(index + 1);
            }
            return addr;
        }

        return "";
    }

    //关闭通道
    public static void closeChannel(TChannel channel) throws Exception {
        channel.close().addListener(future -> LOG.info("close channel..............future:" + future.isSuccessfully()));
    }
}
