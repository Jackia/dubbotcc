package com.kp.dubbotcc.core.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.kp.dubbotcc.core.spring.BeanUtils;

/**
 * 回调方法配置实现
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TccApplicationConfig {
    /**
     * 系统配制
     */
    private ApplicationConfig applicationConfig;
    /**
     * 注册配置
     */
    private RegistryConfig registryConfig;

    private static final TccApplicationConfig INSTANCE = new TccApplicationConfig();

    private TccApplicationConfig() {
        if (INSTANCE != null) {
            throw new Error("error");
        }
    }

    public static TccApplicationConfig getInstance() {
        return INSTANCE;
    }
    /**
     * 获取引用配制
     *
     * @return dubbo 服务调用者  引用
     */
    public ReferenceConfig getConfig(String interfaceName) {
        if (applicationConfig == null) {
            applicationConfig = BeanUtils.getInstance().getBean(ApplicationConfig.class);
        }
        if (registryConfig == null) {
            registryConfig = BeanUtils.getInstance().getBean(RegistryConfig.class);
        }
        ReferenceConfig referenceConfig = new ReferenceConfig();
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setInterface(interfaceName);
        return referenceConfig;
    }
}
