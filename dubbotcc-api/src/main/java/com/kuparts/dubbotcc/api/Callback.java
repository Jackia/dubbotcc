package com.kuparts.dubbotcc.api;

import java.lang.reflect.Method;

/**
 * 回调方法定义
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class Callback {
    /**
     * 标识,
     */
    private String methodName;

    private String className;

    private Mark mark;
    /**
     * 方法
     */
    private Method method;
    /**
     * 回调目标类
     */
    private Class callClazz;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Mark getMark() {
        return mark;
    }

    public void setMark(Mark mark) {
        this.mark = mark;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class getCallClazz() {
        return callClazz;
    }

    public void setCallClazz(Class callClazz) {
        this.callClazz = callClazz;
    }

    /**
     * 标识
     */
    public enum Mark {
        TCCC,//注解
        API//接口实现
    }
}
