package com.kuparts.dubbotcc.commons.config;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;
import com.kuparts.dubbotcc.commons.exception.TccException;

import java.util.concurrent.ExecutionException;

/**
 * dubbo配制缓存
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public final class ApplicationConfigCache {

    int MAX_COUNT = 1000;

    private ApplicationConfigCache() {
    }

    private static class ApplicationConfigCacheInstance {
        static ApplicationConfigCache instance = new ApplicationConfigCache();
    }

    private final LoadingCache<String, ReferenceConfig<?>> cache = CacheBuilder.newBuilder()
            .maximumWeight(MAX_COUNT)
            .weigher((Weigher<String, ReferenceConfig<?>>) (string, ReferenceConfig) -> getSize())
            .build(new CacheLoader<String, ReferenceConfig<?>>() {
                @Override
                public ReferenceConfig<?> load(String key) throws Exception {
                    return TccApplicationConfig.getInstance().getConfig(key);
                }
            });

    private int getSize() {
        if (cache == null) {
            return 0;
        }
        return (int) cache.size();
    }

    /**
     * 获取ApplicationConfigCache对象
     *
     * @return 对象
     */
    public static ApplicationConfigCache getInstance() {

        return ApplicationConfigCacheInstance.instance;
    }

    /**
     * 获取缓存信息
     *
     * @param key 需要获取的key
     */
    public ReferenceConfig<?> get(String key) throws TccException {
        try {
            return cache.get(key);
        } catch (ExecutionException e) {
            throw new TccException(e.getCause());
        }
    }
}
