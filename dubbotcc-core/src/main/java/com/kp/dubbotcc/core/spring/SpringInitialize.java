package com.kp.dubbotcc.core.spring;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.kp.dubbotcc.commons.exception.TccRuntimeException;
import com.kp.dubbotcc.core.config.TccConfig;
import com.kp.dubbotcc.core.cache.TransactionCacheService;
import com.kp.dubbotcc.core.serializer.SerializerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 通过Spring初始化一些基本信息
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@Component
public class SpringInitialize implements ApplicationContextAware {
    private static final Logger LOG = LoggerFactory.getLogger(SpringInitialize.class);
    /**
     * bean操作对象
     */
    private static final BeanUtils beanUtils = BeanUtils.getInstance();
    /**
     * 事务配置一些基本信息
     */
    private TccConfig config;

    public void setConfig(TccConfig config) {
        this.config = config;
    }

    /**
     * 初始Spring信息
     */
    @PostConstruct
    public void init() {
        //判断是否存在这个bean信息
        boolean flag = beanUtils.exitsBean(TccConfig.class);
        if (!flag) {
            TccBeanDefinition definition = new TccBeanDefinition();
            definition.setBeanClass(TccConfig.class);
            beanUtils.registerBean("tccConfig", definition);
        }
        //初始化使用bean
        SerializerFactory.initFactory();
    }

    /**
     * 获取扩展对象
     *
     * @return 缓存使用对象
     */
    @Bean(name = "tcccache", autowire = Autowire.BY_TYPE)
    public TransactionCacheService getCacheBean() {
        TransactionCacheService service;
        if (config == null || StringUtils.isBlank(config.getCache())) {
            service = ExtensionLoader.getExtensionLoader(TransactionCacheService.class).getDefaultExtension();
        } else {
            service = ExtensionLoader.getExtensionLoader(TransactionCacheService.class).getExtension(config.getCache());
            if (service == null) {
                LOG.error("没有加载到缓存对象..cache is null", new TccRuntimeException());
                throw new TccRuntimeException("没有加载到缓存对象..cache is null");
            }
        }
        return service;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        beanUtils.setApplicationContext(applicationContext);
    }
}
