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
    private static final BeanUtils INSTANCE = new BeanUtils();


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
     * @return 防止序列化
     */
    private Object readResolve() {
        return INSTANCE;
    }

    /**
     * 获取一个Bean信息
     *
     * @param type 类型
     * @param <T> 泛型
     * @return 对象
     */
    public <T> T getBean(Class<T> type) {
        Assert.notNull(type);
        return cfgContext.getBean(type);
    }

    /**
     * 获取bean的名字
     * @param type 类型
     * @return  bean名字
     */
    public String getBeanName(Class type){
        Assert.notNull(type);
        return cfgContext.getBeanNamesForType(type)[0];
    }

    /**
     * 判断一个bean是否存在Spring容器中.
     *
     * @param type 类型
     * @return 成功 true 失败 false
     */
    public boolean exitsBean(Class type) {
        Assert.notNull(type);
        return cfgContext.containsBean(type.getName());
    }

    /**
     * 动态注册一个Bean动Spring容器中
     *
     * @param beanName 名称
     * @param beanDefinition 定义bean
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
