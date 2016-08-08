package com.kuparts.dubbotcc.core.major;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.commons.exception.TccRuntimeException;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.core.config.TccConfigBuilder;
import com.kuparts.dubbotcc.core.spring.TCCC;
import com.kuparts.dubbotcc.core.spring.TccSpringConfig;
import com.kuparts.dubbotcc.core.spring.TccTransactionSpring;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * SpringBean管理操作
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class BeanServiceUtils {
    private static final Logger LOG = LoggerFactory.getLogger(BeanServiceUtils.class);

    //扫描包
    private final String SCAN_PACKAGE = "com.kuparts";

    private ConfigurableApplicationContext cfgContext;

    /**
     * 实体对象
     */
    private static final BeanServiceUtils INSTANCE = new BeanServiceUtils();
    /**
     * 初始化实体
     */
    ServiceInitialize initialize;

    private BeanServiceUtils() {
        if (INSTANCE != null) {
            throw new Error("error");
        }
    }

    public static BeanServiceUtils getInstance() {
        return INSTANCE;
    }

    /**
     * 防止序列化产生对象
     *
     * @return 防止序列化
     */
    private Object readResolve() {
        return INSTANCE;
    }

    /**
     * 获取一个Bean信息
     *
     * @param type 类型
     * @param <T>  泛型
     * @return 对象
     */
    public <T> T getBean(Class<T> type) {
        Assert.notNull(type);
        return cfgContext.getBean(type);
    }

    /**
     * 获取bean的名字
     *
     * @param type 类型
     * @return bean名字
     */
    public String getBeanName(Class type) {
        Assert.notNull(type);
        return cfgContext.getBeanNamesForType(type)[0];
    }

    /**
     * 判断一个bean是否存在Spring容器中.
     *
     * @param type 类型
     * @return 成功 true 失败 false
     */
    public boolean exitsBean(Class type) {
        Assert.notNull(type);
        return cfgContext.containsBean(type.getName());
    }

    /**
     * 动态注册一个Bean动Spring容器中
     *
     * @param beanName  名称
     * @param beanClazz 定义bean
     */
    public void registerBean(String beanName, Class beanClazz, Map<String, Object> propertys) {
        Assert.notNull(beanName);
        Assert.notNull(beanClazz);
        boolean flag = exitsBean(TccTransactionSpring.class);
        if (!flag) {
            LOG.error("没有定义com.kuparts.dubbotcc.core.spring.TccTransactionSpring", new TccRuntimeException());
            return;
        }
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) cfgContext.getBeanFactory();
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClazz);
        if (propertys != null) {
            propertys.forEach((k, v) -> builder.addPropertyValue(k, v));
        }
        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
        beanDefinitionRegistry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }

    /**
     * bean对象转换成map对象
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> transBean2Map(Object obj) {
        Assert.notNull(obj);
        Map<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            LOG.error("transBean2Map Error " + e);
        }
        return map;

    }

    /**
     * 动态注册一个Bean动Spring容器中
     *
     * @param beanName  名称
     * @param beanClazz 定义bean
     */
    public void registerBean(String beanName, Class beanClazz) {
        registerBean(beanName, beanClazz, null);
    }

    /**
     * 设置应用上下文
     *
     * @param applicationContext 应用上下文
     * @throws BeansException
     */
    public void setCfgContext(ApplicationContext applicationContext, TccSpringConfig config) throws BeansException {
        cfgContext = (ConfigurableApplicationContext) applicationContext;
        TccConfigBuilder.build(config);
        registerBean();
        if (initialize == null) {
            initialize = getBean(ServiceInitialize.class);
        }
        initialize.init();
        //扫描外部TCCC
        Map<String, Object> beans = cfgContext.getBeansWithAnnotation(TCCC.class);
        loadCallback(beans);
    }


    /**
     * 初始化Bean
     */
    private void registerBean() {
        //注册自定义枚举实现
        //扫描内部定义springbean
        Scanner scanner = new Scanner((BeanDefinitionRegistry) cfgContext.getBeanFactory());
        scanner.setResourceLoader(cfgContext);
        scanner.scan(SCAN_PACKAGE);
    }

    /**
     * 加载回调
     *
     * @param beans beans
     */
    private void loadCallback(Map<String, Object> beans) {
        Assert.notNull(beans);
        initialize.loadCallback(beans);
    }
}
