package com.kuparts.dubbotcc.commons.bean;

import com.kuparts.dubbotcc.commons.config.TccSpringConfig;
import org.springframework.context.ApplicationContext;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public interface TccBeanFactory {
    void loadBean(ApplicationContext context, TccSpringConfig config);
}
