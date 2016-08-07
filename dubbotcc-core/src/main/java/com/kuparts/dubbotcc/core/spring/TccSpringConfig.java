package com.kuparts.dubbotcc.core.spring;

import com.kuparts.dubbotcc.api.CompensationCallback;
import com.kuparts.dubbotcc.core.config.TccConfig;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Properties;

/**
 * spring 需要配制的信息文件
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TccSpringConfig extends TccConfig {
    /**
     * 其它可扩展信息
     */
    private Properties configs = new Properties();
    /**
     * properties文件读取
     */
    private Resource[] locations = {};


    public Properties getConfigs() {
        return configs;
    }

    public void setConfigs(Properties configs) {
        this.configs = configs;
    }

    public Resource[] getLocations() {
        return locations;
    }

    public void setLocations(Resource... locations) {
        this.locations = locations;
    }
}
