package com.kuparts.dubbotcc.core.spring;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.kuparts.dubbotcc.commons.exception.TccRuntimeException;
import com.kuparts.dubbotcc.core.cache.TransactionCacheService;
import com.kuparts.dubbotcc.core.config.ApplicationConfigCache;
import com.kuparts.dubbotcc.core.config.TccConfig;
import com.kuparts.dubbotcc.core.rollback.RollbackService;
import com.kuparts.dubbotcc.core.serializer.SerializerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

/**
 * 通过Spring初始化一些基本信息
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class SpringInitialize implements ApplicationContextAware {
    private static final Logger LOG = LoggerFactory.getLogger(SpringInitialize.class);
    /**
     * bean操作对象
     */
    private static final BeanUtils beanUtils = BeanUtils.getInstance();
    /**
     * 事务配置一些基本信息
     */
    private TccConfig tccConfig;

    public void setTccConfig(TccConfig tccConfig) {
        this.tccConfig = tccConfig;
    }

    public TccConfig getTccConfig() {
        return tccConfig;
    }

    /**
     * 初始Spring信息
     */
    @PostConstruct
    public void init() {
        if (tccConfig == null) {
            String beanName = BeanUtils.getInstance().getBeanName(SpringInitialize.class);
            TccBeanDefinition beanDefinition = new TccBeanDefinition();
            beanDefinition.setParentName(beanName);
            beanDefinition.setBeanClass(TccConfig.class);
            BeanUtils.getInstance().registerBean("tccConfig", beanDefinition);
        }
        //初始化使用bean
        SerializerFactory.initFactory();
        //初始化ApplicationConfig
        ApplicationConfigCache.getInstance().load();
        //启动回滚队列
    }

    /**
     * 获取扩展对象
     *
     * @return 缓存使用对象
     */
    @Bean(name = "tcccache", autowire = Autowire.BY_TYPE)
    public TransactionCacheService getCacheBean() {
        TransactionCacheService service;
        if (tccConfig == null || StringUtils.isBlank(tccConfig.getCache())) {
            service = ExtensionLoader.getExtensionLoader(TransactionCacheService.class).getDefaultExtension();
        } else {
            service = ExtensionLoader.getExtensionLoader(TransactionCacheService.class).getExtension(tccConfig.getCache());
            if (service == null) {
                LOG.error("没有加载到缓存对象..cache is null", new TccRuntimeException());
                throw new TccRuntimeException("没有加载到缓存对象..cache is null");
            }
        }
        return service;
    }

    /**
     * 启动回滚队列
     *
     * @return 服务对象
     */
    @Bean(name = "rollback", autowire = Autowire.BY_TYPE)
    public RollbackService getRollbackService() {
        RollbackService rollbackService = new RollbackService();
        rollbackService.listerQueue();
        return rollbackService;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        beanUtils.setApplicationContext(applicationContext);
    }
}
