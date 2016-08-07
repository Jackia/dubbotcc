package com.kuparts.dubbotcc.core.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定方法
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TcccMethod {
    String value();//指定调用方法的名称
}
