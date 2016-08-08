package com.kuparts.dubbotcc.core.config;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.core.major.BeanServiceUtils;
import com.kuparts.dubbotcc.core.spring.TccSpringConfig;
import com.kuparts.dubbotcc.core.spring.TccTransactionSpring;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/**
 * 配置文件构建
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TccConfigBuilder {


    private static final Logger LOG = LoggerFactory.getLogger(TccTransactionSpring.class);


    /**
     * 构建系统的配制信息
     *
     * @param config 全局配制信息
     */
    public static void build(TccSpringConfig config) {
        //注册到bean
        TccExtConfig tccExtConfig = mregeConfig(config);
        try {
            Map<String, Object> mapValues = BeanServiceUtils.transBean2Map(tccExtConfig);
            BeanServiceUtils.getInstance().registerBean(TccExtConfig.class.getName(), TccExtConfig.class, mapValues);
        } catch (Exception e) {
            LOG.error("loader properties error ," + e.getMessage());
        }
    }

    //构建实体对象
    private static TccExtConfig mregeConfig(TccSpringConfig config) {
        TccExtConfig extconf = new TccExtConfig();
        Properties result = config.getConfigs();
        //属性copy
        try {
            BeanUtils.copyProperties(extconf, config);
        } catch (IllegalAccessException e) {
            LOG.error("loader properties error ," + e.getMessage());
        } catch (InvocationTargetException e) {
            LOG.error("loader properties error ," + e.getMessage());
        }
        //properties合并
        Arrays.stream(config.getLocations()).forEach(e -> {
            try {
                PropertiesLoaderUtils.fillProperties(result, e);
            } catch (IOException e1) {
                LOG.error("loader properties error ," + e1.getMessage());
            }
        });
        //properties转换成bean
        fillBean(result, extconf);
        return extconf;
    }

    /**
     * properties转换成bean
     *
     * @param properties 文件
     */
    private static void fillBean(Properties properties, TccExtConfig extconf) {
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
}
