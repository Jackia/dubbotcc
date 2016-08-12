package com.kuparts.dubbotcc;

import com.kuparts.dubbotcc.commons.config.TccExtConfig;
import com.kuparts.dubbotcc.supervise.support.ParentSuperviseService;
import org.junit.Test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * project：dubbotcc-parent /www.kuparts.com
 * Created By chenbin on 2016/8/12 10:46
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class NettyTest {
    @Test
    public void test() throws InterruptedException {
        TccExtConfig config = new TccExtConfig();
        config.setSerializer("kryo");
        config.setCache("mongo");
        config.setRollbackQueueMax(200);
        config.setRollbackThreadMax(10);
        config.setZookurl("zookeeper://10.1.1.197:2181?backup=10.1.1.198:2181,10.1.1.199:2181");
        new ParentSuperviseService(config).start();
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void testIP() throws SocketException {
        Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) continue;
            Enumeration addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address) {
                    System.out.println(ip.getHostAddress());
                }
            }
        }
    }
}
