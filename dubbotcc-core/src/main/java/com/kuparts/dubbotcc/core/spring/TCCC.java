package com.kuparts.dubbotcc.core.spring;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于指定回滚后所通知本地的回调方法
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface TCCC {
}
