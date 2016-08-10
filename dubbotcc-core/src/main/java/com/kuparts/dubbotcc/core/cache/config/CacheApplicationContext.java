package com.kuparts.dubbotcc.core.cache.config;

import com.alibaba.dubbo.common.extension.SPI;
import com.kuparts.dubbotcc.core.config.TccExtConfig;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 缓存配置初始化信息
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@SPI
public interface CacheApplicationContext {
    /**
     * 对于不同的SPI缓存进行初始化,
     *
     * @param context Spring context
     * @param config  配置
     * @return 初始化后的对象
     */
    Object initialize(ConfigurableApplicationContext context, TccExtConfig config) throws Exception;
}
