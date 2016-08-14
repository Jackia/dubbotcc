package com.kuparts.dubbotcc.core.serializer;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.kuparts.dubbotcc.commons.bean.BeanServiceUtils;
import com.kuparts.dubbotcc.commons.config.TccExtConfig;
import com.kuparts.dubbotcc.commons.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化工厂,获取序列化对象
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@Component
public class SerializerFactory {

    private transient static final Map<String, Integer> SERIDS = new HashMap<>();
    private transient static final Map<Integer, String> STRSTRS = new HashMap<>();

    static {
        //初始关系
        SERIDS.put("kryo", 1);
        SERIDS.put("java", 2);
        SERIDS.put("hessian", 3);
        STRSTRS.put(1, "kryo");
        STRSTRS.put(2, "java");
        STRSTRS.put(3, "hessian");
    }

    @Autowired
    TccExtConfig config;

    /**
     * 初始化一个序列化对象
     */
    public void initFactory() {
        ObjectSerializer serializer;
        if (StringUtils.isBlank(config.getSerializer())) {
            serializer = ExtensionLoader.getExtensionLoader(ObjectSerializer.class).getDefaultExtension();
            config.setSerializer(ExtensionLoader.getExtensionLoader(ObjectSerializer.class).getDefaultExtensionName());
        } else {
            serializer = ExtensionLoader.getExtensionLoader(ObjectSerializer.class).getExtension(config.getSerializer());
        }
        BeanServiceUtils.getInstance().registerBean(ObjectSerializer.class.getName(), serializer);
    }

    /**
     * 根据名称找ID
     *
     * @param serStr
     * @return
     */
    public static int getSerId(String serStr) {
        Assert.notNull(serStr);
        return SERIDS.get(serStr);
    }

    /**
     * 根据id找名称
     *
     * @param id
     * @return
     */
    public static String getStrs(int id) {
        Assert.checkConditionArgument(id >= 0, "id is error");
        return STRSTRS.get(id);
    }
}
