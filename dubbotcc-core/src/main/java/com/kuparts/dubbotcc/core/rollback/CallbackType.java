package com.kuparts.dubbotcc.core.rollback;

import java.lang.reflect.Method;

/**
 * 回调方法定义
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class CallbackType {
    /**
     * 标识,
     */
    private Mark mark;
    /**
     * 方法
     */
    private Method method;
    /**
     * 回调目标类
     */
    private Class callClazz;

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
}

/**
 * 标识
 */
enum Mark {
    TCCC,//注解
    API//接口实现
}
