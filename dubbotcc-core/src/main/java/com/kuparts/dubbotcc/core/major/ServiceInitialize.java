package com.kuparts.dubbotcc.core.major;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.kuparts.dubbotcc.commons.exception.TccRuntimeException;
import com.kuparts.dubbotcc.core.cache.TransactionCacheService;
import com.kuparts.dubbotcc.core.config.TccExtConfig;
import com.kuparts.dubbotcc.core.rollback.RollbackService;
import com.kuparts.dubbotcc.core.serializer.SerializerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 初始化一些服务
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@Component
public class ServiceInitialize {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceInitialize.class);

    @Autowired
    TccExtConfig confg;

    @Autowired
    RollbackService rollback;

    @Autowired
    SerializerFactory serializerFactory;

    /**
     * 初始化服务
     */
    public void init() {
        //合并回调方法信息
        rollback.listerQueue();
        serializerFactory.initFactory();
    }

    @PostConstruct
    public void befour() {
        initCache();
    }

    /**
     * 获取扩展对象
     */
    public void initCache() {
        TransactionCacheService service;
        if (StringUtils.isBlank(confg.getCache())) {
            service = ExtensionLoader.getExtensionLoader(TransactionCacheService.class).getDefaultExtension();
        } else {
            service = ExtensionLoader.getExtensionLoader(TransactionCacheService.class).getExtension(confg.getCache());
            if (service == null) {
                LOG.error("没有加载到缓存对象..cache is null", new TccRuntimeException());
                throw new TccRuntimeException("没有加载到缓存对象..cache is null");
            }
        }
        BeanServiceUtils.getInstance().registerBean(TransactionCacheService.class.getName(), service.getClass());
    }

    /**
     * 初始化序列化对象
     */
    public void initSeriaalizer() {

    }

    public void loadCallback(Map<String, Object> beans) {
        rollback.fullCallback(confg.getCallbacks());
        rollback.fullCallback(beans);
    }
}
