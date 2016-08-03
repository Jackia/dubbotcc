package com.kp.dubbotcc.core.support;

import com.alibaba.dubbo.rpc.RpcContext;

/**
 * DemoServiceImpl
 */

public class DemoServiceImpl implements DemoService {
    public DemoServiceImpl() {
        super();
    }

    public void sayHello(String name) {
        System.out.println("hello " + name);
    }

    public String echo(String text) {
        return text;
    }

    public long timestamp() {
        return System.currentTimeMillis();
    }

    public String getThreadName() {
        return Thread.currentThread().getName();
    }

    public int getSize(String[] strs) {
        if (strs == null)
            return -1;
        return strs.length;
    }

    public int getSize(Object[] os) {
        if (os == null)
            return -1;
        return os.length;
    }

    public Object invoke(String service, String method) throws Exception {
        System.out.println("RpcContext.getContext().getRemoteHost()=" + RpcContext.getContext().getRemoteHost());
        return service + ":" + method;
    }


    public int stringLength(String str) {
        return str.length();
    }


    public byte getbyte(byte arg) {
        return arg;
    }

}