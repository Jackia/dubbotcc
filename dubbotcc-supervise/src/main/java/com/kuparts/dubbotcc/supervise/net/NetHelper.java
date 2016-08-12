package com.kuparts.dubbotcc.supervise.net;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.supervise.TChannel;

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
     * @return
     */
    public static Set<String> getlocalAddress() {
        Set<String> locals = new HashSet<>();
        try {
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) continue;
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {
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
        if (null == channel) {
            return "";
        }
        final SocketAddress remote = channel.remoteAddress();
        final String addr = remote != null ? remote.toString() : "";

        if (addr.length() > 0) {
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
