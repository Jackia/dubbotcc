package com.kp.dubbotcc.core.spring;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kp.dubbotcc.commons.exception.TccRuntimeException;
import com.kp.dubbotcc.commons.utils.Assert;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * SpringBean管理操作
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class BeanUtils {
    private static final Logger LOG = LoggerFactory.getLogger(BeanUtils.class);
    private ConfigurableApplicationContext cfgContext;
    /**
     * 实体对象
     */
    private final static BeanUtils INSTANCE = new BeanUtils();


    private BeanUtils() {
        if (INSTANCE != null) {
            throw new Error("error");
        }
    }

    public static BeanUtils getInstance() {
        return INSTANCE;
    }

    /**
     * 防止序列化产生对象
     * @return
     */
    private Object readResolve() {
        return INSTANCE;
    }

    /**
     * 获取一个Bean信息
     *
     * @param type
     * @param <T>
     * @return
     */
    public <T> T getBean(Class<T> type) {
        Assert.notNull(type);
        return cfgContext.getBean(type);
    }

    /**
     * 判断一个bean是否存在Spring容器中.
     *
     * @param type
     * @return
     */
    public boolean exitsBean(Class type) {
        Assert.notNull(type);
        return cfgContext.containsBean(type.getName());
    }

    /**
     * 动态注册一个Bean动Spring容器中
     *
     * @param beanName
     * @param beanDefinition
     */
    public void registerBean(String beanName, BeanDefinition beanDefinition) {
        Assert.notNull(beanDefinition);
        Assert.notNull(beanName);
        String[] names = cfgContext.getBeanNamesForType(SpringInitialize.class);
        if (names == null || names.length <= 0) {
            LOG.error("没有定义com.kp.dubbotcc.core.spring.SpringInitialize", new TccRuntimeException());
            return;
        }
        beanDefinition.setParentName(names[0]);
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) cfgContext.getBeanFactory();
        beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        cfgContext = (ConfigurableApplicationContext) applicationContext;
    }
}
