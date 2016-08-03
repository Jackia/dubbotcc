package com.kp.dubbotcc.core.filter;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcResult;
import com.kp.dubbotcc.core.support.DemoService;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

/**
 * project：dubbotcc-parent /www.kuparts.com
 * Created By chenbin on 2016/8/3 13:57
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TransactionFilterTest {
    Filter filter = new TransactionFilter();

    @Test
    public void testEcho() {
        Class clazz[] = {String.class};
        Invocation invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethodName()).thenReturn("echo");
        Mockito.when(invocation.getParameterTypes()).thenReturn(clazz);
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{"hello"});
        Mockito.when(invocation.getAttachments()).thenReturn(null);
        Invoker<DemoService> invoker = Mockito.mock(Invoker.class);
        Mockito.when(invoker.isAvailable()).thenReturn(true);
        Mockito.when(invoker.getInterface()).thenReturn(DemoService.class);
        RpcResult result = new RpcResult();
        result.setValue("High");
        Mockito.when(invoker.invoke(invocation)).thenReturn(result);
        URL url = URL.valueOf("test://test:11/test?group=dubbo&version=1.1");
        Mockito.when(invoker.getUrl()).thenReturn(url);
        Result filterResult = filter.invoke(invoker, invocation);
        assertEquals("hello", filterResult.getValue());
    }

}
