package com.kuparts.dubbotcc.core.config;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.core.major.BeanServiceUtils;
import com.kuparts.dubbotcc.core.spring.TccSpringConfig;
import com.kuparts.dubbotcc.core.spring.TccTransactionSpring;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

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
        TccExtConfig tccExtConfig = mergeConfig(config);
        try {
            Map<String, Object> mapValues = BeanServiceUtils.transBean2Map(tccExtConfig);
            BeanServiceUtils.getInstance().registerBean(TccExtConfig.class.getName(), tccExtConfig);
        } catch (Exception e) {
            LOG.error("loader properties error ," + e.getMessage());
        }
    }

    //构建实体对象
    private static TccExtConfig mergeConfig(TccSpringConfig config) {
        TccExtConfig extant = new TccExtConfig();
        Properties result = config.getConfigs();
        //属性copy
        try {
            BeanUtils.copyProperties(extant, config);
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
        fillBean(result, extant);
        return extant;
    }

    /**
     * properties转换成bean
     *
     * @param properties 文件
     */
    private static void fillBean(Properties properties, TccExtConfig extconf) {
        parseProps(properties, extconf);//填充值
        //检查并设置默认值
        //回滚队列数
        if (extconf.getRollbackQueueMax() <= 0) {
            extconf.setRollbackQueueMax(5000);
        }
        //监听队列线程数
        if (extconf.getRollbackThreadMax() <= 0) {
            extconf.setRollbackThreadMax(Runtime.getRuntime().availableProcessors() << 1);
        }
    }

    /**
     * 把属性转换为bean
     *
     * @param properties
     * @param extconf
     */
    private static void parseProps(Properties properties, TccExtConfig extconf) {
        Field[] fields = FieldUtils.getAllFields(TccExtConfigConstants.class);
        Map<String, Object> valueMap = Arrays.stream(fields).map(e -> {
            String fieldValue = null;
            try {
                fieldValue = (String) FieldUtils.readStaticField(e);
                MapEntity entiy = new MapEntity();
                entiy.setKey(fieldValue);
                entiy.setValue(properties.get(fieldValue));
                return entiy;
            } catch (IllegalAccessException e1) {
                LOG.error("loader properties error ," + e1.getMessage());
                return null;
            }
        }).filter(e -> e != null && e.getKey() != null && e.getValue() != null)
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        //把map转成bean
        try {
            BeanUtils.populate(extconf, valueMap);
        } catch (Exception e) {
            LOG.error("loader properties error ," + e.getMessage());
        }
    }

    static class MapEntity {
        String key;
        Object value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
