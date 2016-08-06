package com.kp.dubbotcc.core.config;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;
import com.kp.dubbotcc.commons.exception.TccException;

import java.util.concurrent.ExecutionException;

/**
 * dubbo配制缓存
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class ApplicationConfigCache {

    private final int MAX_COUNT = 1000;

    private ApplicationConfigCache() {
    }

    private static class ApplicationConfigCacheInstance {
        static ApplicationConfigCache instance = new ApplicationConfigCache();
    }

    private LoadingCache<String, ReferenceConfig<?>> cache;

    /**
     * 获取ApplicationConfigCache对象
     *
     * @return 对象
     */
    public static ApplicationConfigCache getInstance() {

        return ApplicationConfigCacheInstance.instance;
    }

    /**
     * 加入配制信息缓存信息
     */
    public void load() {
        cache = CacheBuilder.newBuilder()
                .maximumWeight(MAX_COUNT)
                .weigher((Weigher<String, ReferenceConfig<?>>) (string, ReferenceConfig) -> {
                    if (cache == null) {
                        return 0;
                    }
                    return (int) cache.size();
                }).build(new CacheLoader<String, ReferenceConfig<?>>() {
                    @Override
                    public ReferenceConfig<?> load(String key) throws Exception {
                        return TccApplicationConfig.getInstance().getConfig(key);
                    }
                });
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
