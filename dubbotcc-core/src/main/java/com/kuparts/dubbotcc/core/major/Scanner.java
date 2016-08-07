package com.kuparts.dubbotcc.core.major;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

/**
 * 自定义扫描类
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class Scanner extends ClassPathBeanDefinitionScanner {
    public Scanner(BeanDefinitionRegistry registry) {
        super(registry);
    }
}
