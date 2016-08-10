package com.kuparts.dubbotcc.core.cache.config;

import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.core.cache.TransactionConverter;
import com.kuparts.dubbotcc.core.config.TccExtConfig;
import com.kuparts.dubbotcc.core.major.BeanServiceUtils;
import com.kuparts.dubbotcc.core.serializer.ObjectSerializer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 系统初始化
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public abstract class AbstractCacheApplicationContext implements CacheApplicationContext {
    private transient BeanDefinitionRegistry registry;
    private transient ConfigurableApplicationContext applicationContext;
    /**
     * 数据转换对象
     */
    protected TransactionConverter converter;

    private Object cacheTemp;

    /**
     * 对于不同的SPI缓存进行初始化,
     *
     * @param config 配置
     * @return 初始化后的对象
     */
    public abstract Object init$(TccExtConfig config) throws Exception;

    /**
     * 数据
     *
     * @return
     */
    protected abstract TransactionConverter initConvert();

    @Override
    public Object initialize(ConfigurableApplicationContext context, TccExtConfig config) throws Exception {
        Assert.notNull(context);
        Assert.notNull(config);
        registry = (BeanDefinitionRegistry) context.getParentBeanFactory();
        applicationContext = context;
        converter = initConvert();
        converter.setSerializer(BeanServiceUtils.getInstance().getBean(ObjectSerializer.class));
        cacheTemp = init$(config);
        return cacheTemp;
    }

    public Object getCacheTemp() {
        return cacheTemp;
    }

    public TransactionConverter getConverter() {
        return converter;
    }

    /**
     * 注册Bean信息
     *
     * @param beanDefinition
     */
    protected void registerBean(BeanDefinition beanDefinition) {
        Assert.notNull(beanDefinition);
        registry.registerBeanDefinition(beanDefinition.getBeanClassName(), beanDefinition);

    }

    protected void registerBean(String beanName, Object obj) {
        Assert.notNull(beanName);
        Assert.notNull(obj);
        applicationContext.getBeanFactory().registerSingleton(beanName, obj);
    }
}
