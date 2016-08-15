package com.kuparts.dubbotcc.core.major;

import com.kuparts.dubbotcc.commons.bean.BeanServiceUtils;
import com.kuparts.dubbotcc.commons.bean.TccBeanFactory;
import com.kuparts.dubbotcc.commons.config.TccConfigBuilder;
import com.kuparts.dubbotcc.commons.config.TccSpringConfig;
import com.kuparts.dubbotcc.core.dispatch.propety.CommandType;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class BeanUtilsFactory implements TccBeanFactory {
    //扫描包
    private final String SCAN_PACKAGE = "com.kuparts.dubbotcc";

    private ConfigurableApplicationContext cfgContext;
    /**
     * 初始化实体
     */
    ServiceInitialize initialize;

    /**
     * 初始化Bean
     */
    private void scannerBean() {
        //注册自定义枚举实现
        //扫描内部定义springbean
        Scanner scanner = new Scanner((BeanDefinitionRegistry) cfgContext.getBeanFactory());
        scanner.setResourceLoader(cfgContext);
        scanner.scan(SCAN_PACKAGE);
        //扫描事务调度命令
        CommandScanner commandScanner = new CommandScanner((BeanDefinitionRegistry) cfgContext.getBeanFactory());
        commandScanner.setResourceLoader(cfgContext);
        commandScanner.addIncludeFilter(new AnnotationTypeFilter(CommandType.CommandTask.class));
        commandScanner.scan(SCAN_PACKAGE);
    }

    @Override
    public void loadBean(ApplicationContext context, TccSpringConfig config) {
        synchronized (SCAN_PACKAGE) {
            cfgContext = (ConfigurableApplicationContext) context;
            BeanServiceUtils.getInstance().setCfgContext(cfgContext);
            TccConfigBuilder.build(config);
            scannerBean();
            if (initialize == null) {
                initialize = BeanServiceUtils.getInstance().getBean(ServiceInitialize.class);
            }
            initialize.init(cfgContext);
        }
    }
}
