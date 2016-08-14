package com.kuparts.dubbotcc.core.major;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.google.common.collect.Lists;
import com.kuparts.dubbotcc.commons.api.CompensationCallback;
import com.kuparts.dubbotcc.commons.api.SuperviseService;
import com.kuparts.dubbotcc.commons.bean.BeanServiceUtils;
import com.kuparts.dubbotcc.commons.bean.TCCC;
import com.kuparts.dubbotcc.commons.config.TccExtConfig;
import com.kuparts.dubbotcc.commons.exception.TccRuntimeException;
import com.kuparts.dubbotcc.core.cache.TransactionCacheService;
import com.kuparts.dubbotcc.core.cache.config.CacheApplicationContext;
import com.kuparts.dubbotcc.core.rollback.CallbackService;
import com.kuparts.dubbotcc.core.rollback.RollbackService;
import com.kuparts.dubbotcc.core.rollback.task.DefaultTask;
import com.kuparts.dubbotcc.core.serializer.SerializerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 初始化一些服务
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@Service
public class ServiceInitialize {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceInitialize.class);

    @Autowired
    TccExtConfig config;

    @Autowired
    RollbackService rollback;

    @Autowired
    SerializerFactory serializerFactory;

    @Autowired
    CallbackService callbackService;

    @Autowired
    SuperviseService superviseService;


    private ConfigurableApplicationContext cfgContext;

    /**
     * 初始化服务
     */
    public void init(ConfigurableApplicationContext applicationContext) {
        try {
            this.cfgContext = applicationContext;
            //合并回调方法信息
            serializerFactory.initFactory();
            initCache();
            rollback.listerQueue();
            loadCallback();
            //启动net与Zookeeper
            if (config.getSupervise().equals(Boolean.toString(true))) {
                superviseService.start();
            }
        } catch (RuntimeException ex) {
            LOG.error(ex.getMessage(), ex.getCause());
            System.exit(1);//非正常关闭
        }
    }

    /**
     * 获取扩展对象
     */
    public void initCache() {
        TransactionCacheService service;
        String cacheConfig;
        if (StringUtils.isBlank(config.getCache())) {
            service = ExtensionLoader.getExtensionLoader(TransactionCacheService.class).getDefaultExtension();
            cacheConfig = ExtensionLoader.getExtensionLoader(CacheApplicationContext.class).getDefaultExtensionName();
        } else {
            service = ExtensionLoader.getExtensionLoader(TransactionCacheService.class).getExtension(config.getCache());
            if (service == null) {
                LOG.error("没有加载到缓存对象..cache is null");
                throw new TccRuntimeException("没有加载到缓存对象..cache is null");
            }
            cacheConfig = config.getCache();
        }
        if (StringUtils.isBlank(cacheConfig)) {
            LOG.error("cacheApplicationContext is null");
            throw new TccRuntimeException("cacheApplicationContext is null");
        }
        config.setCache(cacheConfig);
//        service.init(cfgContext, config);
        BeanServiceUtils.getInstance().registerBean(TransactionCacheService.class.getName(), service);
    }

    /**
     * 加载回调
     */
    private void loadCallback() {
        //扫描外部TCCC
        Map<String, Object> beans = cfgContext.getBeansWithAnnotation(DefaultTask.class);
        loadCallBackByDefault(beans);
        beans = cfgContext.getBeansWithAnnotation(TCCC.class);
        loadCallback(beans);//加载用户自定义回调
        loadCallbackByConfig();//加载用户配置文件回调
    }

    /**
     * 加载默认回调
     *
     * @param beans 默认beans
     * @see com.kuparts.dubbotcc.core.rollback.task.DefaultTask
     */
    public void loadCallBackByDefault(Map<String, Object> beans) {
        if (beans == null) {
            return;
        }
        List<CompensationCallback> list = Lists.newArrayList();
        beans.forEach((k, v) -> {
            if (v instanceof CompensationCallback) {
                list.add((CompensationCallback) v);
            }
        });
        callbackService.fullCallback(list);
    }

    /**
     * 加载回调方法实体
     *
     * @param beans beans对象
     * @see com.kuparts.dubbotcc.commons.bean.TCCC
     * @see com.kuparts.dubbotcc.commons.bean.TcccMethod
     */
    public void loadCallback(Map<String, Object> beans) {
        if (beans == null) {
            return;
        }
        callbackService.fullCallback(beans);
    }

    /**
     * 加载配置文件文件
     *
     * @see com.kuparts.dubbotcc.commons.bean.TCCC
     * @see com.kuparts.dubbotcc.commons.bean.TcccMethod
     */
    public void loadCallbackByConfig() {
        callbackService.fullCallback(config.getCallbacks());
    }
}
