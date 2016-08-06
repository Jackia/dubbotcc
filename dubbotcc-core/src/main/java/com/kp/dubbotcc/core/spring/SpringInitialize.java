package com.kp.dubbotcc.core.spring;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.kp.dubbotcc.commons.exception.TccRuntimeException;
import com.kp.dubbotcc.core.cache.TransactionCacheService;
import com.kp.dubbotcc.core.config.ApplicationConfigCache;
import com.kp.dubbotcc.core.config.TccConfig;
import com.kp.dubbotcc.core.rollback.RollbackService;
import com.kp.dubbotcc.core.serializer.SerializerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

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
    public void init() {
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
     * @return
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
