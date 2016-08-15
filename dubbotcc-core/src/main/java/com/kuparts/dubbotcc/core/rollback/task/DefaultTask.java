package com.kuparts.dubbotcc.core.rollback.task;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识系统默认回调任务执行
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface DefaultTask {
}
