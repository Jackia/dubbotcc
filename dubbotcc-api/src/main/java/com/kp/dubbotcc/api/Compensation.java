package com.kp.dubbotcc.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定如果失败的补偿方法
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Compensation {
    String value();
}
