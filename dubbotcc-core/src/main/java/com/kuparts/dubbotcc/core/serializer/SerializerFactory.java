package com.kuparts.dubbotcc.core.serializer;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.kuparts.dubbotcc.commons.bean.BeanServiceUtils;
import com.kuparts.dubbotcc.commons.config.TccExtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 序列化工厂,获取序列化对象
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@Component
public class SerializerFactory {


    @Autowired
    TccExtConfig config;

    /**
     * 初始化一个序列化对象
     */
    public void initFactory() {
        ObjectSerializer serializer;
        if (StringUtils.isBlank(config.getSerializer())) {
            serializer = ExtensionLoader.getExtensionLoader(ObjectSerializer.class).getDefaultExtension();
        } else {
            serializer = ExtensionLoader.getExtensionLoader(ObjectSerializer.class).getExtension(config.getSerializer());
        }
        BeanServiceUtils.getInstance().registerBean(ObjectSerializer.class.getName(), serializer);
    }
}
