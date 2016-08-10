package com.kuparts.dubbotcc.commons.api;

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

    public Callback() {
    }

    public Callback(Mark mark, Method method, Class callClazz, Object bean) {
        this.mark = mark;
        this.method = method;
        this.callClazz = callClazz;
        this.bean = bean;
        this.methodName = method.getName();
        this.className = callClazz.getName();
    }

    /**
     * 方法
     */
    private Method method;
    /**
     * 回调目标类
     */
    private Class callClazz;
    /**
     * 目标bean
     */
    private Object bean;

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

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    /**
     * 标识
     */
    public enum Mark {
        TCCC,//注解
        API//接口实现
    }
}
