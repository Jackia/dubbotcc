package com.kuparts.dubbotcc.core.rollback;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.reflect.Reflection;
import com.kuparts.dubbotcc.api.Callback;
import com.kuparts.dubbotcc.api.CompensationCallback;
import com.kuparts.dubbotcc.api.TccResponse;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.core.spring.TCCC;
import com.kuparts.dubbotcc.core.spring.TcccMethod;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 开始业务回调
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@Component
public class CallbackService {

    private static final Logger LOG = LoggerFactory.getLogger(CallbackService.class);
    /**
     * 回滚后的回调操作..
     */
    private final List<Callback> CALLBACKS = new CopyOnWriteArrayList<>();

    /**
     * 回调过滤列表
     */
    private final List<String> FILTERS = new ArrayList<>();

    /**
     * bean方法填充
     *
     * @param callbacks 回调方法集合
     */
    public void fullCallback(Map<String, Object> callbacks) {
        if (callbacks == null) return;
        callbacks.forEach((k, v) -> {
            Method[] methods = v.getClass().getMethods();
            Assert.notNull(methods);
            Arrays.stream(methods).filter(m -> m.getAnnotation(TcccMethod.class) != null &&
                    m.getParameterTypes().length == 1 &&
                    m.getParameterTypes()[0].getName().equals(TccResponse.class.getName()))
                    .forEach(m -> {
                        try {
                            TcccMethod tcc = m.getAnnotation(TcccMethod.class);
                            Callback t = new Callback();
                            t.setMethod(m);
                            t.setCallClazz(v.getClass());
                            t.setMark(Callback.Mark.TCCC);
                            String methodName = tcc.value();
                            String className = v.getClass().getAnnotation(TCCC.class).value().getName();
                            t.setMethodName(methodName);
                            t.setClassName(className);
                            t.setBean(v);
                            CALLBACKS.add(t);
                            //加入过滤列表
                            addFilters(className);
                        } catch (Exception ex) {
                            LOG.error("load callback error " + ex.getMessage());
                        }

                    });
        });
    }

    /**
     * 获取全类名,前缀 com.kuparts.core // com.kuparts
     *
     * @param className
     * @return
     */
    private String getSpliterPackage(String className) {
        try {
            List<String> result = Splitter.on(".").limit(3).splitToList(className);
            return Joiner.on(".").join(result.subList(0, 2));
        } catch (Exception ex) {
            LOG.error("split package error :classname  " + className + ",advice use java specification named," + ex.getMessage());
        }
        return "";
    }

    /**
     * 实现接口填充
     *
     * @param list 配置文件设置
     */
    public void fullCallback(List<CompensationCallback> list) {
        if (list == null) return;
        list.forEach(e -> {
            Callback t = new Callback();
            t.setCallClazz(e.getClass());
            t.setMark(Callback.Mark.API);
            try {
                Method m = e.getClass().getMethod("callback", TccResponse.class);
                t.setMethod(m);
                t.setClassName(e.getClass().getName());
                t.setMethodName(m.getName());
                t.setBean(e);
                CALLBACKS.add(t);
                //加入过滤列表
                addFilters(e.getClass().getName());
            } catch (NoSuchMethodException e1) {
                LOG.info(e1.getMessage());
            } catch (RuntimeException e1) {
                LOG.info(e1.getMessage());
            }

        });
    }

    private void addFilters(String name) {
        Assert.notNull(name);
        String packPrefix = getSpliterPackage(name);
        if (StringUtils.isBlank(packPrefix)) return;
        if (!FILTERS.contains(packPrefix)) {
            FILTERS.add(packPrefix);
        }
    }

    /**
     * 返回TCCC标识的回调
     *
     * @param element
     * @return
     */
    public List<Callback> stackCallback(StackTraceElement element) {
        return CALLBACKS.stream().filter(e -> e.getMark() == Callback.Mark.TCCC)
                .filter(e -> e.getMethodName().equals(element.getMethodName())
                        && e.getClassName().equals(element.getClassName()))
                .collect(Collectors.toList());
    }

    /**
     * 过滤可能匹配的方法
     *
     * @param e
     * @return 有 true,false
     */
    public boolean existCallback(StackTraceElement e) {
        return FILTERS.stream().anyMatch(
                f -> e.getClassName().startsWith(f));
    }

    /**
     * 判断传入的回调方法,是否存在
     *
     * @param callback 回调方法
     * @return true false
     */
    private Callback existCallback(Callback callback) {
        return CALLBACKS.stream().filter(e -> e.getClassName().equals(callback.getClassName()) && e.getMethod().equals(callback.getMethod())).findFirst().get();
    }

    /**
     * 开始执行回调
     *
     * @param callback 事务事带的回调
     */
    public void execute(Callback callback, TccResponse response) {
        //执行API实现
        CALLBACKS.stream().filter(e -> e.getMark() == Callback.Mark.API).forEach(e -> {

            try {
                CompensationCallback apiCallback = Reflection.newProxy(CompensationCallback.class, new TccProxy(e.getBean()));
                Assert.notNull(apiCallback);
                apiCallback.callback(response);
            } catch (Exception e1) {
                LOG.error("callback execute failure:classname  " + e.getClassName() + "," + e1.getMessage());
            }
        });
        //调用TCCC回调
        Optional<Callback> optional = Optional.empty();
        Callback c1 = optional.orElseGet(() -> existCallback(callback));
        if (c1 == null) return;
        try {
            c1.getMethod().invoke(c1.getBean(), response);
        } catch (Exception e) {
            LOG.error("callback execute failure:classname  " + c1.getClassName() + "," + e.getMessage());
        }
    }

    class TccProxy implements InvocationHandler {
        private Object comCallback;

        public TccProxy(Object comCallback) {
            this.comCallback = comCallback;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(comCallback, args);
        }
    }
}
