package com.kuparts.dubbotcc.core.filter;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcResult;
import com.kuparts.dubbotcc.core.support.DemoService;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

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
        result.setValue("hello");
        Mockito.when(invoker.invoke(invocation)).thenReturn(result);
        URL url = URL.valueOf("test://127.0.0.1:29800/test?group=dubbo&version=1.1");
        Mockito.when(invoker.getUrl()).thenReturn(url);
        RpcContext context = Mockito.mock(RpcContext.class);
        Mockito.when(context.isConsumerSide()).thenReturn(true);
        Mockito.when(context.getLocalAddressString()).thenReturn("127.0.0.1");
        Mockito.when(context.getLocalPort()).thenReturn(29800);
        PowerMockito.mockStatic(RpcContext.class);
        PowerMockito.when(RpcContext.getContext()).thenReturn(context);
        Result filterResult = filter.invoke(invoker, invocation);
        assertEquals("hello", filterResult.getValue());
    }

    @Test
    public void getConfig() {
        String url = "dubbo://localhost:20924/com.kp.facade.auth.service.ResourceService?application=kp-service-api-c&check=false&dubbo=2.8.4&group=auth&interface=com.kp.facade.auth.service.ResourceService&logger=slf4j&methods=validateMethod,tccValidateMethod,validateAll,validateScope,validateLogin&pid=200420&retries=0&revision=0.0.1&side=consumer&timestamp=1470466885203&version=0.0.1";
        ReferenceConfig reference = new ReferenceConfig();
        reference.setUrl(url);
        reference.get();
    }
}
