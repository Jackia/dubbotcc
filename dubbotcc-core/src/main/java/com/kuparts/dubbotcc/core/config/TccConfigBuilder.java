package com.kuparts.dubbotcc.core.config;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.core.major.BeanServiceUtils;
import com.kuparts.dubbotcc.core.spring.TccSpringConfig;
import com.kuparts.dubbotcc.core.spring.TccTransactionSpring;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * 配置文件构建
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TccConfigBuilder {

    private static TccExtConfig extconf;

    private static final Logger LOG = LoggerFactory.getLogger(TccTransactionSpring.class);

    public TccConfigBuilder() {

        extconf = new TccExtConfig();
    }

    /**
     * 构建系统的配制信息
     *
     * @param config 全局配制信息
     */
    public void build(TccSpringConfig config) {
        synchronized (LOG) {
            Properties result = config.getConfigs();
            //属性copy
            BeanUtils.copyProperties(config, extconf);
            //properties合并
            Arrays.stream(config.getLocations()).forEach(e -> {
                try {
                    PropertiesLoaderUtils.fillProperties(result, e);
                } catch (IOException e1) {
                    LOG.error("loader propertes error ," + e1.getMessage());
                }
            });
            //properties转换成bean
            fillBean(result);
            BeanServiceUtils.getInstance().registerBean(TccExtConfig.class.getName(), extconf.getClass());
        }
    }

    /**
     * properties转换成bean
     *
     * @param properties 文件
     */
    private void fillBean(Properties properties) {
        String result = properties.getProperty(TccExtConfigConstants.ROLLBACK_QUEUE_MAX);
        if (StringUtils.isBlank(result)) {
            extconf.setRollbackQueueMax(5000);
        } else {
            extconf.setRollbackQueueMax(Integer.parseInt(result));
        }
        result = properties.getProperty(TccExtConfigConstants.ROLLBACK_THREAD_MAX);
        if (StringUtils.isBlank(result)) {
            extconf.setRollbackThreadMax(Runtime.getRuntime().availableProcessors() << 1);
        } else {
            extconf.setRollbackThreadMax(Integer.parseInt(result));
        }
    }

    /**
     * 获取配置信息
     *
     * @return 系统配置信息
     */

    public TccExtConfig getConfig() {
        return BeanServiceUtils.getInstance().getBean(TccExtConfig.class);
    }
}
