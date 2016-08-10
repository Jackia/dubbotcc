package com.kuparts.dubbotcc.core.cache;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.commons.exception.TccRuntimeException;
import com.kuparts.dubbotcc.core.cache.config.AbstractCacheApplicationContext;
import com.kuparts.dubbotcc.core.cache.config.CacheApplicationContext;
import com.kuparts.dubbotcc.core.config.TccExtConfig;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public abstract class AbstractTransactionCacheService implements TransactionCacheService {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractTransactionCacheService.class);
    /**
     * 转换对象
     */
    protected TransactionConverter converter;
    /**
     * 配制初始化对象
     */
    protected AbstractCacheApplicationContext context;

    /**
     * 返回缓存名字
     *
     * @return 名字
     */
    protected abstract String getCacheName();

    /**
     * 设置操作缓存对象
     *
     * @param obj 缓存对象
     */
    protected abstract void init0(Object obj);


    private void setCacheApplicationContext(CacheApplicationContext cacheApplicationContext) {
        this.context = (AbstractCacheApplicationContext) cacheApplicationContext;
        this.converter = this.context.getConverter();
    }

    @Override
    public void init(ConfigurableApplicationContext cfgContext, TccExtConfig config) {
        if (config.getCache().equals(getCacheName())) {
            CacheApplicationContext cacheContext =
                    ExtensionLoader.getExtensionLoader(CacheApplicationContext.class).getExtension(config.getCache());
            try {
                Object obj = cacheContext.initialize(cfgContext, config);//初始化cacheContext信息
                this.setCacheApplicationContext(cacheContext);//设置当前的context
                init0(obj);
            } catch (Exception e) {
                LOG.error("init cacheApplicationContext error.. " + e.getMessage());
                throw new TccRuntimeException(e.getCause());
            }
        } else {
            LOG.error("init transactionCache error , cache name error");
            throw new TccRuntimeException("init transactionCache error , cache name error");
        }
    }
}
