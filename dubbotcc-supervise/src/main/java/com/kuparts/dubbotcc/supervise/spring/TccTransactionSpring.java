package com.kuparts.dubbotcc.supervise.spring;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.commons.config.TccSpringConfig;
import com.kuparts.dubbotcc.core.major.BeanUtilsFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 通过Spring初始化一些基本信息
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TccTransactionSpring extends TccSpringConfig implements ApplicationContextAware {


    private static final Logger LOG = LoggerFactory.getLogger(TccTransactionSpring.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        new BeanUtilsFactory().loadBean(applicationContext, this);
    }
}
