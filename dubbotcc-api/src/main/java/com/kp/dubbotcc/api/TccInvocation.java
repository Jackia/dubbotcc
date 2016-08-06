package com.kp.dubbotcc.api;

import java.io.Serializable;

/**
 * 执行事务信息
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TccInvocation implements Serializable {
    private static final long serialVersionUID = 7722060715819141844L;
    /**
     * 事务执行器
     */
    private Class targetClazz;
    /**
     * 方法
     */
    private String method;
    /**
     * 参数值
     */
    private Object[] argumentValues;
    /**
     * 参数类型
     */
    private Class[] argumentTypes;

    public TccInvocation() {
    }

    public TccInvocation(Class targetClazz, String method, Object[] argumentValues, Class[] argumentTypes) {
        this.targetClazz = targetClazz;
        this.method = method;
        this.argumentValues = argumentValues;
        this.argumentTypes = argumentTypes;
    }

    public Class getTargetClazz() {
        return targetClazz;
    }

    public String getMethod() {
        return method;
    }

    public Object[] getArgumentValues() {
        return argumentValues;
    }

    public Class[] getArgumentTypes() {
        return argumentTypes;
    }
}
