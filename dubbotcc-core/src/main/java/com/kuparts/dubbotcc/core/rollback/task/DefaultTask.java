package com.kuparts.dubbotcc.core.rollback.task;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * project：dubbotcc-parent /www.kuparts.com
 * Created By chenbin on 2016/8/8 17:24
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
