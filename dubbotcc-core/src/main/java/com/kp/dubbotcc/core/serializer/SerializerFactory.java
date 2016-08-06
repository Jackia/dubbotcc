package com.kp.dubbotcc.core.serializer;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.kp.dubbotcc.core.config.TccConfig;
import com.kp.dubbotcc.core.spring.BeanUtils;

/**
 * 序列化工厂,获取序列化对象
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class SerializerFactory {
    /**
     * 序列化对象
     */
    private static ObjectSerializer serializer;


    /**
     * 初始化一个序列化对象
     */
    public static synchronized void initFactory() {
        TccConfig config = BeanUtils.getInstance().getBean(TccConfig.class);
        if (StringUtils.isBlank(config.getSerializer())) {
            serializer = ExtensionLoader.getExtensionLoader(ObjectSerializer.class).getDefaultExtension();
        } else {
            serializer = ExtensionLoader.getExtensionLoader(ObjectSerializer.class).getExtension(config.getSerializer());
        }
    }

    /**
     * 获取序列化对象
     *
     * @return 序列化对象
     */
    public static ObjectSerializer serializer() {
        return serializer;
    }
}
