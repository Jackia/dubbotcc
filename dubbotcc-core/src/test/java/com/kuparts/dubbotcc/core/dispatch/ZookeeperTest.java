package com.kuparts.dubbotcc.core.dispatch;

import com.kuparts.dubbotcc.commons.config.TccExtConfig;
import com.kuparts.dubbotcc.core.dispatch.propety.Actor;
import com.kuparts.dubbotcc.core.dispatch.propety.Context;
import com.kuparts.dubbotcc.core.dispatch.support.ParentSuperviseService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

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
@RunWith(MockitoJUnitRunner.class)
public class ZookeeperTest {

    @Before
    public void init() {
        System.setProperty("dubbo.application.logger", "slf4j");
    }

    @Test
    public void test() throws InterruptedException {
        TccExtConfig config = new TccExtConfig();
        config.setSerializer("kryo");
        config.setCache("mongo");
        config.setApplication("dubbotcctest");
        config.setRollbackQueueMax(200);
        config.setRollbackThreadMax(10);
        config.setZookurl("zookeeper://192.168.71.130:2181?backup=192.168.71.130:2182,192.168.71.130:2183");
        new ParentSuperviseService(config).test("1111");
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void test2() throws InterruptedException {
        TccExtConfig config = new TccExtConfig();
        config.setSerializer("kryo");
        config.setCache("mongo");
        config.setApplication("dubbotcctest");
        config.setRollbackQueueMax(200);
        config.setRollbackThreadMax(10);
        config.setZookurl("zookeeper://192.168.71.130:2181?backup=192.168.71.130:2182,192.168.71.130:2183");
        new ParentSuperviseService(config).test("2222");
        Thread.sleep(Integer.MAX_VALUE);
    }


    @Test
    public void test3() throws InterruptedException {
        TccExtConfig config = new TccExtConfig();
        config.setSerializer("kryo");
        config.setCache("mongo");
        config.setApplication("dubbotcctest");
        config.setRollbackQueueMax(200);
        config.setRollbackThreadMax(10);
        config.setZookurl("zookeeper://192.168.71.130:2181?backup=192.168.71.130:2182,192.168.71.130:2183");
        new ParentSuperviseService(config).test("3333");
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void testIP() throws SocketException {
        Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                continue;
            }
            Enumeration addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address) {
                    System.out.println(ip.getHostAddress());
                }
            }
        }
    }

    public static void main(String[] args) {
        Context context = Context.getContext();
        Actor actor = new Actor();
        /*Thread t1 = new Thread(()->{
            context.getActors();
            System.out.println(System.currentTimeMillis());
        });
        Thread t2 = new Thread(()->{
            context.getActors();
            System.out.println(System.currentTimeMillis());
        });
        Thread t3 = new Thread(()->{
            context.getActors();
            System.out.println(System.currentTimeMillis());
        });
        t1.start();t2.start();t3.start();*/

        Thread t1 = new Thread(() -> {
            context.getActors();
            System.out.println(System.currentTimeMillis() + "读");
        });
        Thread t2 = new Thread(() -> {
            context.putActor(actor);
            System.out.println(System.currentTimeMillis() + "写");
        });
        Thread t3 = new Thread(() -> {
            context.putIfActor(actor);
            System.out.println(System.currentTimeMillis() + "写");
        });
        t1.start();
        t2.start();
        t3.start();
    }
}
